package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.service.DashboardSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
    @Autowired
    private DashboardSevice dashboardSevice;
    @GetMapping("/get")
    public ResponseEntity<?> getDashBoard(){
        return dashboardSevice.getDashBoard();
    }
}
