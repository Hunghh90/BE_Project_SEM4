package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.GetProgramsDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProgramService {
    ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto);

    ResponseEntity<?> listProgram(GetProgramsDto GetProgramDto);
    ResponseEntity<?> updateProgram(HttpServletRequest request, Long id, UpdateProgramDto updateProgramDto);
    void blockProgram(Long id);
    boolean checkProgramByProgramName(String programName);

    List<ProgramEntity> searchAllField(String value);
    ProgramEntity FindById(Long id);
    ProgramEntity FindByUser(UserEntity userEntity);
}
