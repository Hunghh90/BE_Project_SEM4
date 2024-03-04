package com.example.beprojectsem4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.beprojectsem4.dtos.partnerDtos.GetPartnersDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.dtos.subprogramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.dtos.subprogramDtos.GetSubProgramsDto;
import com.example.beprojectsem4.dtos.subprogramDtos.UpdateSubProgramsDto;
import com.example.beprojectsem4.service.SubProgramService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/subprogram")
public class SubProgramController {
	@Autowired
	private SubProgramService subprogramService;
	
	 @PostMapping("/create-subprogram")
	public ResponseEntity<?> createSubProgram(@RequestBody CreateSubProgramDto createSubProgramDto) {
        return subprogramService.createSubprogram(createSubProgramDto);
    }
	 @GetMapping("/get-subprogram")
	    public ResponseEntity<?> getSubProgram(@RequestParam("id") Long id){
	        return subprogramService.getSubprogram(id);
	    }
	 @GetMapping("/get-all-subprogram")
	    public ResponseEntity<?> listSubprogram(@Nullable GetSubProgramsDto getSubProgramsDto){
	        return subprogramService.listSubprogram(getSubProgramsDto);
	    }
	  @PostMapping("/update-subprogram")
	    public ResponseEntity<?> updateSubprogram(@RequestParam("id")Long id, @RequestBody UpdateSubProgramsDto updateSubProgramsDto){
	        return subprogramService.updateSubprogram(id, updateSubProgramsDto);
	    }
	  @DeleteMapping("/delete-subprogram")
	    public ResponseEntity<?> deleteSubProgram(@RequestParam("id")Long id) {
		  return subprogramService.deleteSubprogram(id);
	  }
}
