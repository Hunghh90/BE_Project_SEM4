package com.example.beprojectsem4.dtos.programDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProgramsDto {
    @Nullable
    private String programName;
    @Nullable
    private int page;
    @Nullable
    private int size;
}
