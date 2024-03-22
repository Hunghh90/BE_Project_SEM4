package com.example.beprojectsem4.dtos.programDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectProgramDto {
    @Nullable
    private String reasonRejection;
    private String value;
}
