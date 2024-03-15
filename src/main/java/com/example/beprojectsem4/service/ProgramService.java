package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.GetProgramsDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProgramService {
    ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto);

    ResponseEntity<?> listProgram(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> updateProgram(HttpServletRequest request, Long id, UpdateProgramDto updateProgramDto);
    ResponseEntity<?> blockProgram(Long id);
    ResponseEntity<?> activekProgram(Long id);
    ResponseEntity<?> listProgramByStatus(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    boolean checkProgramByProgramName(String programName);

    ResponseEntity<?> searchAllField(String value);
    ResponseEntity<?> detailProgram(Long id);
    ProgramEntity addMoneyDonate(CreateDonateDto donateDto);
}
