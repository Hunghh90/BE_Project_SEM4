package com.example.beprojectsem4.dtos.feedbackDtos;

import java.util.Date;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedBackDto {
	 @Nullable
	 private Long userId;
	 @Nullable
	 private String content;
	 @Nullable
	 private String type;
	 @Nullable
	 private Long programId;  	 
	 @Nullable
	 private Date updatedAt;

}