package com.example.beprojectsem4.dtos.dashboardDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDashBoardDto {
    private int totalProgram;
    private int newProgramInMonth;
    private int totalPartner;
    private int newPartnerInMonth;
    private int totalUser;
    private int newUserInMonth;
    private int programActive;
    private int programFinish;
    private int programPending;
    private List<Map<String, Object>> combinedData;
}
