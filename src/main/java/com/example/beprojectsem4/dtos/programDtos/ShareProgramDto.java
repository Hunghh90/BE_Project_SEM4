package com.example.beprojectsem4.dtos.programDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareProgramDto {
    private List<String> emails;
    private String url;
    private Long programId;
}
