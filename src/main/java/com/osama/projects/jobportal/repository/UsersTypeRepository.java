package com.osama.projects.jobportal.repository;

import com.osama.projects.jobportal.domain.model.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersTypeRepository extends JpaRepository<UsersType,Integer> {
}