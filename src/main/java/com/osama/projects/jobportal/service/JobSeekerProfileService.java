package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.JobSeekerProfile;

import java.util.Optional;

public interface JobSeekerProfileService {

    Optional<JobSeekerProfile> getOne(Integer id);

    JobSeekerProfile save(JobSeekerProfile profile);
}