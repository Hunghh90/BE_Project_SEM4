package com.example.beprojectsem4.dtos.feedBackDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private String content;
    private Long programId;
}
