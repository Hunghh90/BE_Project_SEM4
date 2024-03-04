package com.example.beprojectsem4.dtos.subprogramDtos;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSubProgramsDto {
	
	    @Nullable
	    private String type;
	    @Nullable
	    private int page;
	    @Nullable
	    private int size;	
}
