package com.osama.projects.jobportal.controller;

import com.osama.projects.jobportal.domain.model.*;
import com.osama.projects.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        if (jobDetails == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getJobCandidates(jobDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile user = recruiterProfileService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", jobSeekerApplyList);
                }
            } else {
                JobSeekerProfile user = jobSeekerProfileService.getCurrentSeekerProfile();
                if (user != null) {
                    boolean exists = jobSeekerApplyList.stream()
                            .anyMatch(a -> Objects.equals(a.getJobSeekerProfile().getUserAccountId(), user.getUserAccountId()));
                    boolean saved = jobSeekerSaveList.stream()
                            .anyMatch(s -> Objects.equals(s.getJobSeekerProfile().getUserAccountId(), user.getUserAccountId()));
                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }

        model.addAttribute("applyJob", new JobSeekerApply()); // coverLetter form binding, etc.
        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }

    @PostMapping("dashboard/{id}/edit")
    public String editJobForm(@PathVariable("id") int id, Model model, RedirectAttributes ra) {
        JobPostActivity job = jobPostActivityService.getOne(id);
        if (job == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // Only the posting recruiter can edit
        RecruiterProfile me = recruiterProfileService.getCurrentRecruiterProfile();
        if (me == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        // Depending on your mapping, adapt the ownership check:
        // if (job.getPostedBy() == null || !Objects.equals(job.getPostedBy().getUserId(), me.getUserAccountId())) ...
        if (!Objects.equals(job.getPostedById(), me.getUserAccountId())) { // if you store just the int id
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        model.addAttribute("jobPost", job);   // form-backing object expected by add-jobs.html
        return "add-jobs";
    }

    @PostMapping("/dashboard/{id}")
    public String editJob(@PathVariable("id") int id,
                          @ModelAttribute("jobPost") JobPostActivity form,
                          RedirectAttributes ra) {

        JobPostActivity db = jobPostActivityService.getOne(id);
        if (db == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        RecruiterProfile me = recruiterProfileService.getCurrentRecruiterProfile();
        if (me == null || !Objects.equals(db.getPostedById(), me.getUserAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Copy editable fields (add/remove as needed)
        db.setJobTitle(form.getJobTitle());
        db.setDescriptionOfJob(form.getDescriptionOfJob());
        db.setJobType(form.getJobType());
        db.setRemote(form.getRemote());
        db.setSalary(form.getSalary());
        db.setJobCompany(form.getJobCompany());
        db.setJobLocation(form.getJobLocation());

        jobPostActivityService.save(db); // or update(...)

        ra.addFlashAttribute("success", "Job post updated.");
        return "redirect:/dashboard/";
    }

    @PostMapping("/job-details/apply/{id}")
    public String applyJob(@PathVariable("id") int id,
                           @ModelAttribute("applyJob") JobSeekerApply form, // coverLetter from the form
                           RedirectAttributes ra) {

        JobPostActivity job = jobPostActivityService.getOne(id);
        if (job == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        JobSeekerProfile seeker = jobSeekerProfileService.getCurrentSeekerProfile();
        if (seeker == null) {
            ra.addFlashAttribute("error", "Please complete your candidate profile first.");
            return "redirect:/job-seeker-profile/add"; // adjust to your profile route
        }

        // Prevent duplicate apply
        boolean alreadyApplied = jobSeekerApplyService.getJobCandidates(job).stream()
                .anyMatch(a -> Objects.equals(a.getJobSeekerProfile().getUserAccountId(), seeker.getUserAccountId()));
        if (alreadyApplied) {
            ra.addFlashAttribute("info", "You have already applied to this job.");
            return "redirect:/job-details-apply/" + id;
        }

        JobSeekerApply apply = new JobSeekerApply();
        apply.setJobSeekerProfile(seeker);
        apply.setJobPostActivity(job);
        if (StringUtils.hasText(form.getCoverLetter())) {
            apply.setCoverLetter(form.getCoverLetter());
        }
        apply.setApplyDate(new Date());

        jobSeekerApplyService.save(apply); // or addNew(...)

        ra.addFlashAttribute("success", "Application submitted.");
        return "redirect:/dashboard/";
    }
}