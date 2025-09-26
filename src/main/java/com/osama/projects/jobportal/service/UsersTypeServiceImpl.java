package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.UsersType;
import com.osama.projects.jobportal.repository.UsersTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersTypeServiceImpl implements UsersTypeService{

    private final UsersTypeRepository userTypeRepository;

    @Override
    public List<UsersType> getAll(){
        return userTypeRepository.findAll();
    }


}