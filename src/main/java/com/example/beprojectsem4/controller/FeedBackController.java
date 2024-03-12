package com.example.beprojectsem4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.beprojectsem4.dtos.feedbackDtos.CreateFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.GetFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.UpdateFeedBackDto;
import com.example.beprojectsem4.service.FeedBackService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {
	@Autowired
	private FeedBackService feedBackService;
	
	 @PostMapping("/create-feedback")
		public ResponseEntity<?> createFeedBack(@RequestBody CreateFeedBackDto createFeedBackDto,HttpServletRequest request) {
	        return feedBackService.createFeedBack(createFeedBackDto,request);
	    }
		 @GetMapping("/get-feedback")
		    public ResponseEntity<?> getFeedBack(@RequestParam("id") Long id){
		        return feedBackService.getFeedBack(id);
		    }
		 @GetMapping("/get-all-feedback")
		    public ResponseEntity<?> listFeedBack(@Nullable GetFeedBackDto getFeedBackDto){
		        return feedBackService.listFeedBack(getFeedBackDto);
		    }
		  @PostMapping("/update-feedback")
		    public ResponseEntity<?> updateFeedBack(@RequestParam("id")Long id, @RequestBody UpdateFeedBackDto updateFeedBackDto,HttpServletRequest request){
		        return feedBackService.updateFeedBack(id, updateFeedBackDto,request);
		    }
		  @DeleteMapping("/delete-feedback")
		    public ResponseEntity<?> deleteFeedBack(@RequestParam("id")Long id) {
			  return feedBackService.deleteFeedBack(id);
		  }

}
