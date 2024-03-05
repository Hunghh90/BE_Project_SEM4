package com.example.beprojectsem4.dtos.subprogramDtos;

import java.util.Date;

import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubProgramDto {
	   
	    private Long userId;
	    private String type;
	    private Long programId;  
	    private Date createAt;	  
}
