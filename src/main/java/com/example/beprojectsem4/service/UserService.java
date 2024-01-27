package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.ChangePassword;
import com.example.beprojectsem4.dtos.RegisterDto;
import com.example.beprojectsem4.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createAccount(RegisterDto registerDto);
    ResponseEntity<?> changePassword(HttpServletRequest request, ChangePassword changePassword);
}
