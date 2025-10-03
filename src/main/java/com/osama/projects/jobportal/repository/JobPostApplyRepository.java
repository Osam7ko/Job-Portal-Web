package com.osama.projects.jobportal.repository;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerApply;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByJobSeekerProfile(JobSeekerProfile profile);

    List<JobSeekerApply> findByJobPostActivity(JobPostActivity jobPostActivity);
}