package com.example.beprojectsem4.dtos.feedbackDtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedBackDto {
	  private String content;
	  private String type;
	  private Date createAt;
	  private Long userId;	 
	  private Long programId;  
	

}
