package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.RegisterDto;
import com.example.beprojectsem4.entities.UserEntity;

public interface UserService {
    boolean createAccount(RegisterDto registerDto);
}
