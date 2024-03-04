package com.example.beprojectsem4.service;

import org.springframework.http.ResponseEntity;

import com.example.beprojectsem4.dtos.subprogramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.dtos.subprogramDtos.GetSubProgramsDto;
import com.example.beprojectsem4.dtos.subprogramDtos.UpdateSubProgramsDto;

public interface SubProgramService {

    ResponseEntity<?> createSubprogram(CreateSubProgramDto createSubProgramDto);
    ResponseEntity<?> getSubprogram(Long id);
    ResponseEntity<?> listSubprogram(GetSubProgramsDto getSubProgramsDto);
    ResponseEntity<?> updateSubprogram(Long id, UpdateSubProgramsDto updateSubProgramsDto);
    ResponseEntity<?> deleteSubprogram(Long id);
}
