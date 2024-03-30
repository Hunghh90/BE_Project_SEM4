package com.example.beprojectsem4.dtos.feedBackDto;

import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDto {
    private Long feedBackId;
    private String content;
    private GetMeDto user;
    private Date createdAt;
}
