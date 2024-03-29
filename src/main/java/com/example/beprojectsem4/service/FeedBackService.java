package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.feedBackDto.CreateCommentDto;
import com.example.beprojectsem4.dtos.feedBackDto.EditFeedBackDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface FeedBackService {
    ResponseEntity<?> createComment(HttpServletRequest request, CreateCommentDto createCommentDto);
    ResponseEntity<?> getCommentByProgramId(Long programId, PaginateAndSearchByNameDto paginate);
    ResponseEntity<?> editComment(HttpServletRequest request, EditFeedBackDto edit);
    ResponseEntity<?> deleteComment(HttpServletRequest request,Long id);
}
