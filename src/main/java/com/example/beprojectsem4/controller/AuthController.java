package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserEntity user){
       return authService.register(user);
    }

}
