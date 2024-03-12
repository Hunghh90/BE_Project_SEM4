package com.example.beprojectsem4.dtos.feedbackDtos;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFeedBackDto {
		@Nullable
	    private String content;
		@Nullable
	    private String type;
	    @Nullable
	    private int page;
	    @Nullable
	    private int size;	
}
