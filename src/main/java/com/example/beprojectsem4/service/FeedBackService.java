package com.example.beprojectsem4.service;

import org.springframework.http.ResponseEntity;

import com.example.beprojectsem4.dtos.feedbackDtos.CreateFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.FeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.GetFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.UpdateFeedBackDto;

import jakarta.servlet.http.HttpServletRequest;

public interface FeedBackService {
	ResponseEntity<?> createFeedBack(CreateFeedBackDto createFeedBackDto,HttpServletRequest request);
    ResponseEntity<?> getFeedBack(Long id);
    ResponseEntity<?> listFeedBack(GetFeedBackDto getfeedBackDto);
    ResponseEntity<?> updateFeedBack(Long id, UpdateFeedBackDto updateFeedBackDto,HttpServletRequest request);
    ResponseEntity<?> deleteFeedBack(Long id);
}
