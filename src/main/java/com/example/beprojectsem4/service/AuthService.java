package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.LoginDto;
import com.example.beprojectsem4.dtos.RegisterDto;
import com.example.beprojectsem4.entities.UserEntity;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto registerDto);
    ResponseEntity<?> login(LoginDto login);

}
