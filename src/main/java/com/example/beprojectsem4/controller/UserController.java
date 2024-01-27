package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.ChangePassword;
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

    @GetMapping("/detail")
    public String getDetailUser(){
        return "User";
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request,@RequestBody ChangePassword changePassword) {
        return userService.changePassword(request,changePassword);
    }
}
