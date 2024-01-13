package com.example.beprojectsem4.service;

import com.example.beprojectsem4.entities.UserEntity;

public interface AuthService {
    String register(UserEntity user);
    String login(String email,String password);

}
