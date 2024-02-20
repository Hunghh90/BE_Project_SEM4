package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.authDtos.LoginDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.userDtos.ResetPasswordDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto registerDto);
    ResponseEntity<?> login(LoginDto login);
    public void logout(HttpServletRequest request);
    ResponseEntity<?> activeAccount(String code, String email);
    ResponseEntity<?> forgotPassword(String email);
    ResponseEntity<?> resetPassword(String email, ResetPasswordDto resetPasswordDto);
}
