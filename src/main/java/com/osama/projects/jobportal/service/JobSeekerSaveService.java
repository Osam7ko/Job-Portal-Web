package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.domain.model.JobSeekerSave;

import java.util.List;

public interface JobSeekerSaveService {

    List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId);

    List<JobSeekerSave> getJobCandidates(JobPostActivity job);
}