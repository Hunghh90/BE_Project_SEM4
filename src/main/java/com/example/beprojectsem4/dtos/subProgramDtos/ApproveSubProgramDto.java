package com.example.beprojectsem4.dtos.subProgramDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveSubProgramDto {
    private Long id;
    private String value;
    private String note;
}
