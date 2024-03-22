package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.userDtos.ChangePasswordDto;
import com.example.beprojectsem4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/get-me")
    public ResponseEntity<?> getDetailUser(HttpServletRequest request){
        return userService.getMe(request);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request,@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(request, changePasswordDto);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return userService.refreshToken(request);
    }

    @PostMapping("/create-account-admin")
    public void createAdminAccount(RegisterDto registerDto){
        userService.createAccountAdmin(registerDto);
    }
}
