package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.authDtos.LoginDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto registerDto);
    ResponseEntity<?> login(LoginDto login);

    ResponseEntity<?> activeAccount(String code, String email);

}
