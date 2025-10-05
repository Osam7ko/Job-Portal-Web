package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.domain.model.JobSeekerSave;
import com.osama.projects.jobportal.repository.JobSeekerSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobSeekerSaveServiceImpl implements JobSeekerSaveService {

    private final JobSeekerSaveRepository jobSeekerSaveRepository;


    @Override
    public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId) {
        return jobSeekerSaveRepository.findByJobSeekerProfile(userAccountId);
    }

    @Override
    public List<JobSeekerSave> getJobCandidates(JobPostActivity job) {
        return jobSeekerSaveRepository.findByJobPostActivity(job);
    }

    @Override
    public void addNew(JobSeekerSave jobSeekerSave) {
        jobSeekerSaveRepository.save(jobSeekerSave);
    }

}