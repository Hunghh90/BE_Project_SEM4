package com.example.beprojectsem4.dtos.dashboardDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
