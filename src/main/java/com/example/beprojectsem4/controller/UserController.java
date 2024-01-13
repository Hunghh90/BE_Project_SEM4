package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/detail")
    public String getDetailUser(){
        return "User";
    }
}
