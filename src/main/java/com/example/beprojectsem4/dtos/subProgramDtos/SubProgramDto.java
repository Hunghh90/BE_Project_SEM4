package com.example.beprojectsem4.dtos.subProgramDtos;

import com.example.beprojectsem4.dtos.programDtos.ProgramDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubProgramDto {
    private Long id;
    private String type;
    private String note;
    private ProgramDto program;
    private GetMeDto user;
}
