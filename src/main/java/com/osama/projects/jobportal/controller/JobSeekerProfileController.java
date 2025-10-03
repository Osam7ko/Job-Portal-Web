package com.osama.projects.jobportal.controller;

import com.osama.projects.jobportal.domain.model.JobSeekerExperience;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.domain.model.Skills;
import com.osama.projects.jobportal.domain.model.Users;
import com.osama.projects.jobportal.repository.UsersRepository;
import com.osama.projects.jobportal.service.JobSeekerProfileService;
import com.osama.projects.jobportal.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/job-seeker-profile")
@RequiredArgsConstructor
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;

    private final UsersRepository usersRepository;

    @GetMapping("/")
    public String jobSeekerProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            JobSeekerProfile profile = jobSeekerProfileService.getOne(user.getUserId())
                    .orElseGet(() -> JobSeekerProfile.builder()
                            .usersId(user)
                            .userAccountId(user.getUserId())
                            .build());

            if (profile.getSkills() == null || profile.getSkills().isEmpty()) {
                profile.setSkills(new ArrayList<>(List.of(new Skills())));
            }

            model.addAttribute("profile", profile);
        }

        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("profile") JobSeekerProfile profile,
                         @RequestParam(value = "image", required = false) MultipartFile profileImage,
                         @RequestParam(value = "pdf", required = false) MultipartFile resumePdf,
                         @RequestParam(value = "experienceProofs", required = false) List<MultipartFile> experienceProofs) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            profile.setUsersId(user);
            profile.setUserAccountId(user.getUserId());
        }

        // Clean + wire lists
        if (profile.getSkills() != null) {
            profile.getSkills().removeIf(s ->
                    (s.getName() == null || s.getName().isBlank())
                            && (s.getYearsOfExperience() == null || s.getYearsOfExperience().isBlank())
                            && (s.getExperienceLevel() == null || s.getExperienceLevel().isBlank())
            );
            profile.getSkills().forEach(s -> s.setProfile(profile));
        }

        if (profile.getEducations() != null) {
            profile.getEducations().removeIf(e ->
                    (e.getLevel() == null || e.getLevel().isBlank())
                            && (e.getInstitutionName() == null || e.getInstitutionName().isBlank())
                            && (e.getDegree() == null || e.getDegree().isBlank())
            );
            profile.getEducations().forEach(e -> e.setProfile(profile));
        }

        if (profile.getExperiences() != null) {
            profile.getExperiences().removeIf(x ->
                    (x.getCompany() == null || x.getCompany().isBlank())
                            && (x.getRoleTitle() == null || x.getRoleTitle().isBlank())
                            && (x.getYears() == null)
            );
            profile.getExperiences().forEach(x -> x.setProfile(profile));
        }

        // Filenames before save
        if (profileImage != null && !profileImage.isEmpty()) {
            String fn = org.springframework.util.StringUtils.cleanPath(Objects.requireNonNull(profileImage.getOriginalFilename()));
            profile.setProfilePhoto(fn);
        }
        if (resumePdf != null && !resumePdf.isEmpty()) {
            String fn = org.springframework.util.StringUtils.cleanPath(Objects.requireNonNull(resumePdf.getOriginalFilename()));
            profile.setResume(fn);
        }

        // Experience proofs map by DOM order
        if (profile.getExperiences() != null && experienceProofs != null) {
            int limit = Math.min(profile.getExperiences().size(), experienceProofs.size());
            for (int i = 0; i < limit; i++) {
                MultipartFile f = experienceProofs.get(i);
                if (f != null && !f.isEmpty()) {
                    String fn = org.springframework.util.StringUtils.cleanPath(Objects.requireNonNull(f.getOriginalFilename()));
                    profile.getExperiences().get(i).setProofFile(fn);
                }
            }
        }

        JobSeekerProfile saved = jobSeekerProfileService.save(profile);

        // Save files to disk
        String photosDir = "photos/candidate/" + saved.getUserAccountId();
        String docsDir = "docs/candidate/" + saved.getUserAccountId();
        String expDir = docsDir + "/experience";

        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                FileUploadUtil.saveFile(photosDir, saved.getProfilePhoto(), profileImage);
            }
            if (resumePdf != null && !resumePdf.isEmpty()) {
                FileUploadUtil.saveFile(docsDir, saved.getResume(), resumePdf);
            }
            if (profile.getExperiences() != null && experienceProofs != null) {
                int limit = Math.min(saved.getExperiences().size(), experienceProofs.size());
                for (int i = 0; i < limit; i++) {
                    MultipartFile f = experienceProofs.get(i);
                    JobSeekerExperience exp = saved.getExperiences().get(i);
                    if (f != null && !f.isEmpty() && exp.getProofFile() != null) {
                        FileUploadUtil.saveFile(expDir, exp.getProofFile(), f);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard/";
    }
}