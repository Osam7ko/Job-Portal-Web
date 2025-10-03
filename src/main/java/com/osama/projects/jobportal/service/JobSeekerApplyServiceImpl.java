package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerApply;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.repository.JobPostApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobSeekerApplyServiceImpl implements JobSeekerApplyService {

    private final JobPostApplyRepository jobPostApplyRepository;

    @Override
    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId) {
        return jobPostApplyRepository.findByJobSeekerProfile(userAccountId);
    }

    @Override
    public List<JobSeekerApply> getJobCandidates(JobPostActivity job) {
        return jobPostApplyRepository.findByJobPostActivity(job);
    }

    @Override
    public void save(JobSeekerApply apply) {
        jobPostApplyRepository.save(apply);
    }
}