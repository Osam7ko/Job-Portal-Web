package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.dto.RecruiterJobsDto;
import com.osama.projects.jobportal.domain.model.JobPostActivity;

import java.time.LocalDate;
import java.util.List;

public interface JobPostActivityService {

    JobPostActivity addNew(JobPostActivity jobPostActivity);

    List<RecruiterJobsDto> getRecruiterJobs(int recruiter);

    JobPostActivity getOne(int id);

    List<JobPostActivity> getAll();

    List<JobPostActivity> search(String job, String location, List<String> list, List<String> list1, LocalDate searchDate);

    void save(JobPostActivity db);
}