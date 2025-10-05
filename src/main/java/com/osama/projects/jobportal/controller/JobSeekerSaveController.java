package com.osama.projects.jobportal.controller;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.domain.model.JobSeekerSave;
import com.osama.projects.jobportal.service.JobPostActivityService;
import com.osama.projects.jobportal.service.JobSeekerProfileService;
import com.osama.projects.jobportal.service.JobSeekerSaveService;
import com.osama.projects.jobportal.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobSeekerSaveController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @PostMapping("/job-details/save/{id}")
    public String save(@PathVariable int id, RedirectAttributes ra) {
        // must be logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        // load job by path id
        JobPostActivity job = jobPostActivityService.getOne(id);
        if (job == null) {
            ra.addFlashAttribute("error", "Job not found.");
            return "redirect:/dashboard/";
        }

        // current seeker profile
        JobSeekerProfile seeker = jobSeekerProfileService.getCurrentSeekerProfile();
        if (seeker == null) {
            ra.addFlashAttribute("error", "Please complete your candidate profile first.");
            return "redirect:/job-seeker-profile/";
        }

        // build & save (unique on userId+job)
        JobSeekerSave save = new JobSeekerSave();
        save.setJobSeekerProfile(seeker);
        save.setJobPostActivity(job);

        try {
            jobSeekerSaveService.addNew(save);
            ra.addFlashAttribute("success", "Job saved.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // already saved due to unique constraint
            ra.addFlashAttribute("info", "You already saved this job.");
        }

        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {
        List<JobPostActivity> jobPost = new ArrayList<>();
        Object currentUser = usersService.getCurrentUserProfile();

        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUser);
        for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
            jobPost.add(jobSeekerSave.getJobPostActivity());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", currentUser);
        return "saved-jobs";
    }
}