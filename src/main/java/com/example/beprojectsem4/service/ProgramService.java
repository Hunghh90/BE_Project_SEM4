package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.DonationDtos.CreateDonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.*;
import com.example.beprojectsem4.entities.ProgramEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProgramService {
    ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto);

    ResponseEntity<?> listProgram(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> updateProgram(Long id, UpdateProgramDto updateProgramDto);

    ResponseEntity<?> toggleLockProgram(Long id,String value);
    ResponseEntity<?> approveProgram(Long id, RejectProgramDto rejectProgramDto);
    ResponseEntity<?> listProgramByStatus(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    boolean checkProgramByProgramName(String programName);

    ResponseEntity<?> searchAllField(String value);
    ResponseEntity<?> detailProgram(Long id);
    ProgramEntity addMoneyDonate(CreateDonateDto donateDto);

    ResponseEntity<?> deleteCertify(ListUrlDto files, Long id);
    ResponseEntity<?> shareProgram(HttpServletRequest request,ShareProgramDto shareProgramDto);
    ResponseEntity<?> finishProgram(ListUrlDto files, Long id);
    ResponseEntity<?> extendFinishProgram(Long id, UpdateProgramDto updateProgramDto);
    ProgramEntity findById(Long id);
}
