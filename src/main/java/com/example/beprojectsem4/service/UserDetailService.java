package com.example.beprojectsem4.service;

import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.jwt.UserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.findUserByEmail(email);
        return new UserDetail(user);

    }
}
