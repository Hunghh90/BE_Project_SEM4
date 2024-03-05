package com.example.beprojectsem4.service;

import org.springframework.http.ResponseEntity;

import com.example.beprojectsem4.dtos.subprogramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.dtos.subprogramDtos.GetSubProgramsDto;
import com.example.beprojectsem4.dtos.subprogramDtos.UpdateSubProgramsDto;

import jakarta.servlet.http.HttpServletRequest;

public interface SubProgramService {

    ResponseEntity<?> createSubprogram(CreateSubProgramDto createSubProgramDto,HttpServletRequest request);
    ResponseEntity<?> getSubprogram(Long id);
    ResponseEntity<?> listSubprogram(GetSubProgramsDto getSubProgramsDto);
    ResponseEntity<?> updateSubprogram(Long id, UpdateSubProgramsDto updateSubProgramsDto,HttpServletRequest request);
    ResponseEntity<?> deleteSubprogram(Long id);
}
