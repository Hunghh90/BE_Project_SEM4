package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.userDtos.ChangePasswordDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.dtos.userDtos.ResetPasswordDto;
import com.example.beprojectsem4.dtos.userDtos.UpdateUserDto;
import com.example.beprojectsem4.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    boolean createAccountUser(RegisterDto registerDto);
    ResponseEntity<?> createAccountAdmin(RegisterDto registerDto);
    ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto);
    void saveRefreshToken(String email,String refreshToken);
    ResponseEntity<?> refreshToken(HttpServletRequest request);
    GetMeDto getMe(HttpServletRequest request);
    ResponseEntity<?> updateUser(HttpServletRequest request,UpdateUserDto updateUserDto);
    ResponseEntity<?> blockUser(String email);
    ResponseEntity<?> activeUser(String email);
    UserEntity checkUser(String email);
    UserEntity findUserByToken(HttpServletRequest request);
    boolean resetPassword(String email, ResetPasswordDto resetPasswordDto);
}
