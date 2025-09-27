package com.osama.projects.jobportal.domain.dto;

import com.osama.projects.jobportal.domain.model.JobCompany;
import com.osama.projects.jobportal.domain.model.JobLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterJobsDto {

    private Long totalCandidates;

    private Integer jobPostId;

    private String jobTitle;

    private JobLocation jobLocationId;

    private JobCompany jobCompanyId;

}