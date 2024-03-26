package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.FeedBackDto.CreateCommentDto;
import com.example.beprojectsem4.dtos.FeedBackDto.EditFeedBackDto;
import com.example.beprojectsem4.dtos.FeedBackDto.FeedBackDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.FeedBackEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.FeedBackRepository;
import com.example.beprojectsem4.service.FeedBackService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    private FeedBackRepository feedBackRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProgramService programService;


    @Override
    public ResponseEntity<?> createComment(HttpServletRequest request, CreateCommentDto createCommentDto) {
        Set<String> blacklist = new HashSet<>();
    try {
        Stream<String> stream = Files.lines(Paths.get("src/main/resources/blacklist.txt"));
            stream.forEach(word -> blacklist.add(word.trim()));
        UserEntity user = userService.findUserByToken(request);
            ProgramEntity program = programService.findById(createCommentDto.getProgramId());
        for (String word : blacklist) {
            if (createCommentDto.getContent().toLowerCase().contains(word.toLowerCase())) {
                return ResponseEntity.badRequest().body("Your comment contains prohibited words");
            }
        }
        if(user != null && program != null){
            for(DonationEntity donate : program.getDonations()){
                Long id = donate.getUser().getUserId();
                if(user.getUserId().compareTo(id) == 0){
                    FeedBackEntity feedBack = new FeedBackEntity(createCommentDto.getContent(), user,program);
                    feedBackRepository.save(feedBack);
                    return ResponseEntity.ok().body("Comment success");
                }
            }
        }
        return ResponseEntity.badRequest().body("Comment not success");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getCommentByProgramId(Long programId, PaginateAndSearchByNameDto paginateDto) {
        try {
            PaginateAndSearchByNameDto paginate = new PaginateAndSearchByNameDto(paginateDto.getName(), paginateDto.getPage(), paginateDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(paginate.getPage()-1, paginate.getSize(),sort);
            ProgramEntity program = programService.findById(programId);
            Page<FeedBackEntity> feedBackEntityPage = feedBackRepository.findAllByProgram(program,pageRequest);
            if(feedBackEntityPage.isEmpty()){
                return ResponseEntity.badRequest().body("Not found comment in program");
            }
            List<FeedBackDto> feedBackDtoList = new ArrayList<>();
            for(FeedBackEntity feedBack : feedBackEntityPage){
                FeedBackDto feedBackDto = EntityDtoConverter.convertToDto(feedBack,FeedBackDto.class);
                feedBackDto.setUserName(feedBack.getUser().getDisplayName());
                feedBackDto.setUserId(feedBack.getUser().getUserId());
                feedBackDtoList.add(feedBackDto);
            }
            return ResponseEntity.ok().body(feedBackDtoList);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> editComment(HttpServletRequest request, EditFeedBackDto edit) {
        try {
            Optional<FeedBackEntity> feedBackEntityOptional = feedBackRepository.findById(edit.getFeedBackId());
            UserEntity user = userService.findUserByToken(request);
            if(feedBackEntityOptional.isPresent() ){
                FeedBackEntity feedBack = feedBackEntityOptional.get();
                if(user != null && Objects.equals(user,feedBack.getUser())){
                    feedBack.setContent(edit.getNewContent());
                    feedBackRepository.save(feedBack);
                    return ResponseEntity.ok().body("Edit comment success");
                }
                return ResponseEntity.badRequest().body("You no editing rights");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteComment(HttpServletRequest request, Long id) {
        try{
            Optional<FeedBackEntity> feedBackEntityOptional = feedBackRepository.findById(id);
            UserEntity user = userService.findUserByToken(request);
            if(feedBackEntityOptional.isPresent() ){
                FeedBackEntity feedBack = feedBackEntityOptional.get();
                if(user != null && Objects.equals(user,feedBack.getUser())){
                    feedBackRepository.delete(feedBack);
                    return ResponseEntity.ok().body("Delete comment success");
                }
                return ResponseEntity.badRequest().body("You no delete rights");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
