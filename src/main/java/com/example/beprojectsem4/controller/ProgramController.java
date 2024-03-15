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
    public ResponseEntity<?> getAllProgram(@RequestBody PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return programService.listProgram(paginateAndSearchByNameDto);
    }

    @PostMapping("/create-program")
    public ResponseEntity<?> createProgram(HttpServletRequest request,@RequestBody CreateProgramDto createProgramDto){
        return programService.createProgram(request, createProgramDto);
    }

    @PostMapping("/update-program/{id}")
    public ResponseEntity<?> updateProgram(HttpServletRequest request, @PathVariable Long id, @RequestBody UpdateProgramDto updateProgramDto){
        return programService.updateProgram(request, id, updateProgramDto);
    }

    @PostMapping("/active-program")
    public ResponseEntity<?> activekProgram(@RequestParam Long id){
        return programService.activekProgram(id);
    }

    @PostMapping("/block-program")
    public ResponseEntity<?> blockProgram(@RequestParam Long id){
        return programService.blockProgram(id);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchAllField(@RequestParam("search") String search){
        return programService.searchAllField(search);
    }

    @PostMapping("/list-program-by-status")
    public ResponseEntity<?> listProgramByStatus(@RequestBody PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return programService.listProgramByStatus(paginateAndSearchByNameDto);
    }

    @GetMapping("/detail-program/{id}")
    public ResponseEntity<?> detailProgram(@PathVariable("id") Long id){
        return programService.detailProgram(id);
    }
}
