package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.repository.JobSeekerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobSeekerProfileServiceImpl implements JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    @Override
    public Optional<JobSeekerProfile> getOne(Integer id) {
        return jobSeekerProfileRepository.findById(id);
    }

    @Override
    public JobSeekerProfile save(JobSeekerProfile profile) {
        return jobSeekerProfileRepository.save(profile);
    }
}