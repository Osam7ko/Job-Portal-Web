package com.osama.projects.jobportal.repository;

import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.JobSeekerProfile;
import com.osama.projects.jobportal.domain.model.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    List<JobSeekerSave> findByJobSeekerProfile(JobSeekerProfile profile);

    List<JobSeekerSave> findByJobPostActivity(JobPostActivity jobPostActivity);
}