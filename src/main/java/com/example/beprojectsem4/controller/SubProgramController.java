package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.subProgramDtos.ApproveSubProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.service.SubProgramService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subprogram")
public class SubProgramController {
    @Autowired
    private SubProgramService subProgramService;

    @PostMapping("/register-or-cancel")
    public ResponseEntity<?> registerOrCancel(HttpServletRequest request,@RequestBody CreateSubProgramDto createSubProgramDto){
        return subProgramService.registerOrCancel(request,createSubProgramDto);
    }

    @PostMapping("/approve-volunteer")
    public ResponseEntity<?> approveVolunteer(@RequestBody ApproveSubProgramDto approveSubProgramDto){
        return subProgramService.approveVolunteer(approveSubProgramDto);
    }

    @GetMapping("/get-all-by-user")
    public ResponseEntity<?> getAllByUser(HttpServletRequest request, PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return subProgramService.getAllByUser(request,paginateAndSearchByNameDto);
    }

    @GetMapping("/get-all-by-program/{programId}")
    public ResponseEntity<?> getAllByProgramAndStatus(@PathVariable Long programId,PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return subProgramService.getAllByProgramAndStatus(programId,paginateAndSearchByNameDto);
    }

    @GetMapping("/get-all-field")
    public ResponseEntity<?> getByAllField(@RequestParam String search){
        return subProgramService.getByAllField(search);
    }
}
