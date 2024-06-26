package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.*;
import com.example.beprojectsem4.service.ProgramService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    private ProgramService programService;
    @GetMapping("/get-all-programs")
    public ResponseEntity<?> getAllProgram(@Nullable Long partnerId, PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return programService.listProgram(partnerId,paginateAndSearchByNameDto);
    }

    @PostMapping("/create-program")
    public ResponseEntity<?> createProgram(HttpServletRequest request,@RequestBody CreateProgramDto createProgramDto){
        return programService.createProgram(request, createProgramDto);
    }

    @PostMapping("/update-program/{id}")
    public ResponseEntity<?> updateProgram( @PathVariable Long id, @RequestBody UpdateProgramDto updateProgramDto){
        return programService.updateProgram(id, updateProgramDto);
    }

    @GetMapping("/active-program/{id}")
        public ResponseEntity<?> activeProgram(@PathVariable("id") Long id, RejectProgramDto rejectProgramDto){
        return programService.approveProgram(id,rejectProgramDto);
    }

    @GetMapping("/toggle-lock-program")
    public ResponseEntity<?> toggleLockProgram( Long id,String value){
        return programService.toggleLockProgram(id,value);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAllField( String search,@Nullable Long partnerId){
        return programService.searchAllField(search,partnerId);
    }

    @GetMapping("/list-program-by-status")
    public ResponseEntity<?> listProgramByStatus(@Nullable Long partnerId,  PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return programService.listProgramByStatus(partnerId,paginateAndSearchByNameDto);
    }

    @GetMapping("/detail-program/{id}")
    public ResponseEntity<?> detailProgram(HttpServletRequest request,@PathVariable("id") Long id){
        return programService.detailProgram(request,id);
    }

    @PostMapping("/finish-program/{id}")
    public ResponseEntity<?> finishProgram(@RequestBody ListUrlDto listUrlDto,@PathVariable Long id){
        return programService.finishProgram(listUrlDto,id);
    }

    @PostMapping("/delete-certify/{id}")
    public ResponseEntity<?> deleteCertify(@RequestBody ListUrlDto listUrlDto, @PathVariable Long id){
        return programService.deleteCertify(listUrlDto,id);
    }

    @PostMapping("/extend-program/{id}")
    public ResponseEntity<?> extendFinishProgram(@PathVariable Long id, @RequestBody UpdateProgramDto updateProgramDto){
        return programService.extendFinishProgram(id,updateProgramDto);
    }
    @PostMapping("/share-program")
    public ResponseEntity<?> shareProgram(HttpServletRequest request,@RequestBody ShareProgramDto shareProgramDto){
        return programService.shareProgram(request,shareProgramDto);
    }
}
