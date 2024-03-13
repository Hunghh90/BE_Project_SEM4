package com.example.beprojectsem4.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.beprojectsem4.dtos.feedbackDtos.CreateFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.FeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.GetFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.UpdateFeedBackDto;
import com.example.beprojectsem4.dtos.subprogramDtos.SubProgramsDto;
import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.FeedBackEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.FeedBackRepository;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.FeedBackService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
@Service
public class FeedBackServiceImpl implements FeedBackService {

	@Autowired
	private FeedBackRepository feedBackRepository;
	@Autowired
	private UserService userService;
	@Autowired 
	private ProgramService programService;
	@Autowired
	private DonationService donationService;
	@Override
	public ResponseEntity<?> createFeedBack(CreateFeedBackDto createFeedBackDto,HttpServletRequest request) {
		 try {
		        FeedBackEntity feedback = EntityDtoConverter.convertToEntity(createFeedBackDto, FeedBackEntity.class);
		        String regex = "\\b(vl|vcl|đụ|mẹ|mày|cặc|đỉ|lồn|buồi|đéo|địt|cặc)\\b";
		        if(feedback != null) {
		        	UserEntity user=userService.findUserByToken(request);
		    	    ProgramEntity program=programService.FindById(createFeedBackDto.getProgramId());
		    	    DonationEntity donate=donationService.FindByUser(user);	    	   		    	      	    	   
		    	    if (donate == null) {		    	      
		    	        return ResponseEntity.badRequest().body("User haven't donate");
		    	    } 
		    	    else {				    	    	
			        	 Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			             Matcher matcher = pattern.matcher(createFeedBackDto.getContent());
			             if (matcher.find()) {
			            	 return ResponseEntity.badRequest().body("Users use vulgar words");
			             } else {
			            	    feedback.setUser(user);
				    	    	feedback.setProgram(program);
				    	    	feedBackRepository.save(feedback);
				    	        return ResponseEntity.status(HttpStatus.CREATED).body("Create feedbaack success");
			             }    	    	
		    	    }
		    	    }
		        else {
		        	   return ResponseEntity.badRequest().body("Can't create feedbaack");
		        }       
		 }
		 catch (Exception ex){
    System.out.println(ex.getMessage());
    return ResponseEntity.internalServerError().body(ex.getMessage());
}

	}
	@Override
	public ResponseEntity<?> getFeedBack(Long id) {
		try {
	       Optional<FeedBackEntity> feedback = feedBackRepository.findById(id);
	        if (feedback.isPresent()) {
	        	FeedBackEntity fbEntity=feedback.get();
	        	FeedBackDto subdto=EntityDtoConverter.convertToDto(fbEntity, FeedBackDto.class);
	        	UserEntity user=fbEntity.getUser();
	        	ProgramEntity program=fbEntity.getProgram();
	        	subdto.setUserId(user.getUserId());
	        	subdto.setProgramId(program.getProgramId());
	           return ResponseEntity.ok().body(subdto);
	        } else {
	            return ResponseEntity.badRequest().body("feedback not found");
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        return ResponseEntity.internalServerError().body(e.getMessage());
	    }
	}

	@Override
	public ResponseEntity<?> listFeedBack(GetFeedBackDto getfeedBackDto) {
		  try {
	            Sort sort = Sort.by(Sort.Order.desc("createAt"));
	            PageRequest pageRequest = PageRequest.of(getfeedBackDto.getPage(), getfeedBackDto.getSize(),sort);
	            Page<FeedBackEntity> feedbacks = feedBackRepository.findBytypeContaining(getfeedBackDto.getType(),pageRequest);
	            List<FeedBackDto> feedbacksDtos = new ArrayList<>();
	            for (FeedBackEntity fback : feedbacks){
	                FeedBackDto subproDto = EntityDtoConverter.convertToDto(fback,FeedBackDto.class);
	                feedbacksDtos.add(subproDto);
	            }
	            return ResponseEntity.ok().body(feedbacksDtos);
	        }catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }
	}
	@Override
	public ResponseEntity<?> updateFeedBack(Long id, UpdateFeedBackDto updateFeedBackDto, HttpServletRequest request) {
		  try {
	            Optional<FeedBackEntity> feedback= feedBackRepository.findById(id);
	            String regex = "\\b(vl|vcl|đụ|mẹ|mày|cặc|đỉ|lồn|buồi|đéo|địt|cặc)\\b";
	            if (feedback.isPresent()) {
	            	 Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		             Matcher matcher = pattern.matcher(updateFeedBackDto.getContent());
		             if (matcher.find()) {
		            	 return ResponseEntity.badRequest().body("Users use vulgar words");
		             } else {
		            	 FeedBackEntity feedbk = EntityDtoConverter.convertToEntity(updateFeedBackDto,FeedBackEntity.class);            	
			            	FeedBackEntity feedbkn = TransferValuesIfNull.transferValuesIfNull(feedback.get(),feedbk);            	
			                    UserEntity user=userService.findUserByToken(request);
			                    ProgramEntity program=programService.FindById(updateFeedBackDto.getProgramId());		
					    	    program.setProgramId(updateFeedBackDto.getProgramId());			    	
					    	    feedbkn.setUser(user);
					    	    feedbkn.setProgram(program);	 		           
					    	    feedBackRepository.save(feedbkn);
			                return ResponseEntity.ok().body("Update success");
		             }    	    	
	            	
	            }
	            else {
	            	return ResponseEntity.badRequest().body("Can not update");
	            }
	      
	        }catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }       
	}
	@Override

	public ResponseEntity<?> deleteFeedBack(Long id) {
		  try{
			  Optional<FeedBackEntity> feedBack= feedBackRepository.findById(id);
	        if (feedBack.isPresent()) {
	        	FeedBackEntity feedBackn = feedBack.get();
	        	feedBackRepository.delete(feedBackn);
	            return ResponseEntity.ok().body("Delete success");
	        } else {
	        	return ResponseEntity.badRequest().body("Can't delete cause feedback not found");
	        }}
		  
	        catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }
	       
	}


	
}
