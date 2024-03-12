package com.example.beprojectsem4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import com.example.beprojectsem4.dtos.feedbackDtos.CreateFeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.FeedBackDto;
import com.example.beprojectsem4.dtos.feedbackDtos.UpdateFeedBackDto;
import com.example.beprojectsem4.entities.FeedBackEntity;
import com.example.beprojectsem4.entities.SubProgramEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface FeedBackRepository extends JpaRepository<FeedBackEntity, Long> {
	  Page<FeedBackEntity> findBytypeContaining(String type,@Nullable Pageable pageable);
	
}
