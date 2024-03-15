package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.userDtos.ChangePasswordDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.userDtos.ResetPasswordDto;
import com.example.beprojectsem4.dtos.userDtos.UpdateUserDto;
import com.example.beprojectsem4.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createAccountUser(RegisterDto registerDto);
    ResponseEntity<?> createAccountAdmin(RegisterDto registerDto);
    ResponseEntity<?> createAccountPartner(RegisterDto registerDto);
    ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto);
    void saveRefreshToken(String email,String refreshToken);
    ResponseEntity<?> refreshToken(HttpServletRequest request);
    ResponseEntity<?> getMe(HttpServletRequest request);
    String updateUser(HttpServletRequest request, UpdateUserDto updateUserDto);
    String blockUser(String email);
    boolean activeUser(String email);
    UserEntity checkUser(String email);
    UserEntity findUserById(Long id);
    UserEntity findUserByToken(HttpServletRequest request);
    boolean resetPassword(String email, ResetPasswordDto resetPasswordDto);
}
