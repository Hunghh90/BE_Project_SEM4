package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.ApproveSubProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.CreateSubProgramDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface SubProgramService {
    ResponseEntity<?> registerOrCancel(HttpServletRequest request, CreateSubProgramDto createSubProgramDto);
    ResponseEntity<?> approveVolunteer(ApproveSubProgramDto approveSubProgramDto);
    ResponseEntity<?> getAllByUser(HttpServletRequest request, PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> getAllByProgram(Long programId, PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> getByAllField(String search);
}
