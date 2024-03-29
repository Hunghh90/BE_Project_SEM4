package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.feedBackDto.CreateCommentDto;
import com.example.beprojectsem4.dtos.feedBackDto.EditFeedBackDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.service.FeedBackService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedBackService feedBackService;

    @PostMapping("create-feedback")
    public ResponseEntity<?> createFeedback(HttpServletRequest request,@RequestBody CreateCommentDto createCommentDto){
        return feedBackService.createComment(request,createCommentDto);
    }

    @GetMapping("/get-comment")
    public ResponseEntity<?> getComment(Long programId, PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return feedBackService.getCommentByProgramId(programId,paginateAndSearchByNameDto);
    }

    @PostMapping("/edit-comment")
    public ResponseEntity<?> editComment(HttpServletRequest request, @RequestBody EditFeedBackDto edit){
        return feedBackService.editComment(request,edit);
    }
    @GetMapping("/delete-comment")
    public ResponseEntity<?> deleteComment(HttpServletRequest request,@RequestParam("id") Long id){
        return feedBackService.deleteComment(request, id);
    }
}
