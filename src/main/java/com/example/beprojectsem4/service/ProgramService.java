package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.GetProgramsDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProgramService {
    ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto);

    ResponseEntity<?> listProgram(GetProgramsDto GetProgramDto);
    ResponseEntity<?> updateProgram(HttpServletRequest request, Long id, UpdateProgramDto updateProgramDto);
    ResponseEntity<?> blockProgram(Long id);
    ResponseEntity<?> activekProgram(Long id);
    boolean checkProgramByProgramName(String programName);

    ResponseEntity<?> searchAllField(String value);
}
