package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.RecruiterProfile;

import java.util.Optional;

public interface RecruiterProfileService {

    Optional<RecruiterProfile> getOne(Integer id);

    RecruiterProfile addNew(RecruiterProfile recruiterProfile);

    RecruiterProfile getCurrentRecruiterProfile();
}