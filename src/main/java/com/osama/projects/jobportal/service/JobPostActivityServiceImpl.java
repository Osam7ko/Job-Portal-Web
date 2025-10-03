package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.dto.RecruiterJobsDto;
import com.osama.projects.jobportal.domain.model.JobCompany;
import com.osama.projects.jobportal.domain.model.JobLocation;
import com.osama.projects.jobportal.domain.model.JobPostActivity;
import com.osama.projects.jobportal.domain.model.RecruiterJobs;
import com.osama.projects.jobportal.repository.JobPostActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobPostActivityServiceImpl implements JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    @Override
    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    @Override
    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter) {
        List<RecruiterJobs> recruiterJobsDtos = jobPostActivityRepository.getRecruiterJobs(recruiter);

        List<RecruiterJobsDto> recruiterJobsDtosList = new ArrayList<>();

        for (RecruiterJobs rec : recruiterJobsDtos) {
            JobLocation loc = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
            JobCompany comp = new JobCompany(rec.getCompanyId(), rec.getName(), "", "", "");
            recruiterJobsDtosList.add(new RecruiterJobsDto(rec.getTotalCandidates(), rec.getJob_post_id(), rec.getJob_title(),
                    loc, comp));
        }
        return recruiterJobsDtosList;
    }

    @Override
    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    @Override
    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
        return Objects.isNull(searchDate) ? jobPostActivityRepository.searchWithoutDate(job, location, remote, type)
                : jobPostActivityRepository.search(job, location, remote, type, searchDate);
    }
}