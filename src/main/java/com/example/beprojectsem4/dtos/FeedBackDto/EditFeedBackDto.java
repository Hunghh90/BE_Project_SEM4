package com.example.beprojectsem4.dtos.FeedBackDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditFeedBackDto {
    private Long feedBackId;
    private String newContent;
}
