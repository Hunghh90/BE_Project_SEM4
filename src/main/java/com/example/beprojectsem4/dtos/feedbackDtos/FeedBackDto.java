package com.example.beprojectsem4.dtos.feedbackDtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackDto {
		private Long feedbackId;	  
	    private Long userId;
	    private String type;	 
	    private Long programId;
	    private Date createAt;	  
	    private Date updatedAt;

}
