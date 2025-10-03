package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerApply;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;

import java.util.List;

public interface JobSeekerApplyService {


    List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId);

    List<JobSeekerApply> getJobCandidates(JobPostActivity job);

}