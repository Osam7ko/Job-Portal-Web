package com.osama.projects.jobportal.controller;

import com.osama.projects.jobportal.domain.model.RecruiterProfile;
import com.osama.projects.jobportal.domain.model.Users;
import com.osama.projects.jobportal.repository.UsersRepository;
import com.osama.projects.jobportal.service.RecruiterProfileService;
import com.osama.projects.jobportal.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;

    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() ->
                    new UsernameNotFoundException("Could not " + "found user"));
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
            if (!recruiterProfile.isEmpty()) {
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile,
                         @RequestParam(value = "image", required = false) MultipartFile profileImage,
                         @RequestParam(value = "companyProofImage", required = false) MultipartFile companyProofImage,
                         Model model) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() ->
                    new UsernameNotFoundException("Could not " + "found user"));
            recruiterProfile.setUsersId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", recruiterProfile);

        String profileFileName = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileFileName = StringUtils.cleanPath(Objects.requireNonNull(profileImage.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(profileFileName);
        }
        String prooffFileName = null;
        if (companyProofImage != null && !companyProofImage.isEmpty()) {
            prooffFileName = StringUtils.cleanPath(Objects.requireNonNull(companyProofImage.getOriginalFilename()));
            recruiterProfile.setCompanyProofUrl(prooffFileName);
        }
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);

        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try {
            if (profileFileName != null) {
                FileUploadUtil.saveFile(uploadDir, profileFileName, profileImage);
            }
            if (prooffFileName != null) {
                FileUploadUtil.saveFile(uploadDir, prooffFileName, companyProofImage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        recruiterProfileService.addNew(recruiterProfile);
        return "redirect:/dashboard/";
    }
}