package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.dto.RecruiterJobsDto;
import com.osama.projects.jobportal.domain.model.JobPostActivity;

import java.util.List;

public interface JobPostActivityService {

    JobPostActivity addNew(JobPostActivity jobPostActivity);

    List<RecruiterJobsDto> getRecruiterJobs(int recruiter);
}