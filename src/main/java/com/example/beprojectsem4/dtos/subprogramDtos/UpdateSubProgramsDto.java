package com.example.beprojectsem4.dtos.subprogramDtos;

import java.util.Date;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubProgramsDto {
	 @Nullable
	 private Long userId;
	 @Nullable
	 private String type;
	 @Nullable
	 private Long programId;  	 
	 @Nullable
	 private Date updatedAt;
	
	 
	 
}