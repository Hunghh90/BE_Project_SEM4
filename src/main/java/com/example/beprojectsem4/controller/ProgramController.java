package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.GetProgramsDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import com.example.beprojectsem4.service.ProgramService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    private ProgramService programService;
    @GetMapping("/get-all-programs")
    public ResponseEntity<?> getAllProgram(PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return programService.listProgram(paginateAndSearchByNameDto);
    }

    @PostMapping("/create-program")
    public ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto){
        return programService.createProgram(request, createProgramDto);
    }

    @PostMapping("/update-program")
    public ResponseEntity<?> updateProgram(HttpServletRequest request, Long id, UpdateProgramDto updateProgramDto){
        return programService.updateProgram(request, id, updateProgramDto);
    }

    @PostMapping("/active-program")
    public ResponseEntity<?> activekProgram(Long id){
        return programService.activekProgram(id);
    }

    @PostMapping("/block-program")
    public ResponseEntity<?> blockProgram(Long id){
        return programService.blockProgram(id);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchAllField(String value){
        return programService.searchAllField(value);
    }

    @PostMapping("/list-program-deactive")
    public ResponseEntity<?> listProgramDeActive(PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return programService.listProgramDeActive(paginateAndSearchByNameDto);
    }

//    @GetMapping("/detail-program/{id}")
//    public ResponseEntity<?> detailProgram(@PathVariable("id") Long id){
//        return programService.detailProgram(id);
//    }
}
