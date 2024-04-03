package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.dashboardDto.GetDashBoardDto;
import org.springframework.http.ResponseEntity;

public interface DashboardSevice {
    ResponseEntity<?> getDashBoard();
}
