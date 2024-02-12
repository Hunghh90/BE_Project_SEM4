package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.authDtos.LoginDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.userDtos.ResetPassword;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.service.AuthService;
import com.example.beprojectsem4.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto){
        return authService.register(registerDto);
    }

    @PostMapping("/log-in")
    public ResponseEntity logIn (@RequestBody LoginDto login) {
        return authService.login(login);
    }

    @GetMapping("/log-out")
    public void logOut(HttpServletRequest request){
        authService.logout(request);
    }

    @GetMapping("/active")
    public void active(@RequestParam("code") String code, @RequestParam("email") String email){
        authService.activeAccount(code,email);
    };

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        return authService.forgotPassword(email);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email, @RequestBody ResetPassword resetPassword){
        return authService.resetPassword(email,resetPassword);
    }

}
