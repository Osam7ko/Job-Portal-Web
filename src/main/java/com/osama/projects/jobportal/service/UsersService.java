package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.Users;

import java.util.Optional;

public interface UsersService {

    Users addNew(Users users);

    Optional<Users> getUserByEmail(String email);

    Object getCurrentUserProfile();

    Users getCurrentUser();
}