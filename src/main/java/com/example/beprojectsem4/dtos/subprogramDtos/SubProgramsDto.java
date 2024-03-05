package com.example.beprojectsem4.dtos.subprogramDtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubProgramsDto {
	    private Long subProgramId;	  
	    private Long userId;
	    private String type;	 
	    private Long programId;
	    private Date createAt;	  
	    private Date updatedAt;
	
	    
}
