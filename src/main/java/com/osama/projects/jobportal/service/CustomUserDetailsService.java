package com.osama.projects.jobportal.service;

import com.osama.projects.jobportal.domain.model.Users;
import com.osama.projects.jobportal.repository.UsersRepository;
import com.osama.projects.jobportal.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Could not found user"));
        return new CustomUserDetails(users);
    }
}