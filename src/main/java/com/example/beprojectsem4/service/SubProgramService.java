package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.ApproveSubProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.SubProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubProgramService {
    ResponseEntity<?> registerOrCancel(HttpServletRequest request, CreateSubProgramDto createSubProgramDto);
    ResponseEntity<?> approveVolunteer(ApproveSubProgramDto approveSubProgramDto);
    ResponseEntity<?> getAllByUser(HttpServletRequest request, PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> getAllByProgram(Long programId, PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> getByAllField(String search);
    SubProgramEntity getByUserAndProgramAndType(UserEntity user, ProgramEntity program, String type);
    List<SubProgramEntity> getAllByProgramAndStatus(ProgramEntity program, String status);
}
