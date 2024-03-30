package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.ProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.ApproveSubProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.SubProgramDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.SubProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.DynamicSpecification;
import com.example.beprojectsem4.repository.SubProgramRepository;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.SendEmailService;
import com.example.beprojectsem4.service.SubProgramService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubProgramServiceImpl implements SubProgramService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private SubProgramRepository repository;
    @Autowired
    private SendEmailService sendEmailService;
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<?> registerOrCancel(HttpServletRequest request, CreateSubProgramDto createSubProgramDto) {
        try {
            UserEntity user = userService.findUserByToken(request);
            if(user == null ){
                return ResponseEntity.badRequest().body("User not found");
            }
            ProgramEntity program = programService.findById(createSubProgramDto.getProgramId());
            if(program == null ){
                return ResponseEntity.badRequest().body("Program not found");
            }
            SubProgramEntity existsSubProgram = repository.findByUserAndProgramAndType(user,program,createSubProgramDto.getType());
            if(existsSubProgram !=null){
                if(existsSubProgram.getType().equals("volunteer")){
                    if(existsSubProgram.getStatus().equals("Pending") || existsSubProgram.getStatus().equals("Active")){
                        existsSubProgram.setStatus("Cancel");
                        repository.save(existsSubProgram);
                        return ResponseEntity.ok().body("Cancel success");
                    } else if (existsSubProgram.getStatus().equals("Cancel") ) {
                        existsSubProgram.setStatus("Pending");
                        repository.save(existsSubProgram);
                        return ResponseEntity.ok().body("Registered collaborator successfully");
                    }
                }else{
                    if( existsSubProgram.getStatus().equals("Active")){
                        existsSubProgram.setStatus("Cancel");
                        repository.save(existsSubProgram);
                        return ResponseEntity.ok().body("Cancel success");
                    } else if (existsSubProgram.getStatus().equals("Cancel") ) {
                        existsSubProgram.setStatus("Active");
                        repository.save(existsSubProgram);
                        return ResponseEntity.ok().body("Subscribe program successfully");
                    }
                }
            }
            if(createSubProgramDto.getType().equals("volunteer")){
                if(!program.isRecruitCollaborators()){
                    return ResponseEntity.badRequest().body("Program not recruit collaborators");
                }else {
                    SubProgramEntity subProgram = EntityDtoConverter.convertToEntity(createSubProgramDto,SubProgramEntity.class);
                    subProgram.setUser(user);
                    subProgram.setProgram(program);
                    subProgram.setStatus("Pending");
                    repository.save(subProgram);
                    Resource resource = new ClassPathResource("templates/VolunteerRegistration.html");
                    String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                    String processedHtmlContent = htmlContent
                            .replace("[[Recipient_Name]]", user.getDisplayName())
                            .replace("[[Program_Name]]", program.getProgramName());
                    sendEmailService.sendEmail(user.getEmail(),
                            "Volunteer Job Registration Successful - Await Confirmation",
                            processedHtmlContent);
                    return ResponseEntity.ok().body("Registered collaborator successfully");
                }
            }
            SubProgramEntity subProgram = EntityDtoConverter.convertToEntity(createSubProgramDto,SubProgramEntity.class);
            subProgram.setUser(user);
            subProgram.setProgram(program);
            subProgram.setStatus("Active");
            repository.save(subProgram);
            return ResponseEntity.ok().body("Subscribe program successfully");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> approveVolunteer(ApproveSubProgramDto approveSubProgram) {
        try {
            Optional<SubProgramEntity> subProgramOptional = repository.findById(approveSubProgram.getId());
            if(subProgramOptional.isPresent() && subProgramOptional.get().getStatus().equals("Pending")){
                SubProgramEntity subProgram = subProgramOptional.get();
                subProgram.setStatus(approveSubProgram.getValue());
                subProgram.setNote(approveSubProgram.getNote());
                repository.save(subProgram);
                if(approveSubProgram.getValue().equals("Active") && subProgram.getType().equals("volunteer")){
                    Resource resource = new ClassPathResource("templates/ActiveVolunteer.html");
                    String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                    String processedHtmlContent = htmlContent
                            .replace("[[Recipient_Name]]", subProgram.getUser().getDisplayName())
                            .replace("[[Note]]", subProgram.getNote())
                            .replace("[[Program_Name]]", subProgram.getProgram().getProgramName());
                    sendEmailService.sendEmail(subProgram.getUser().getEmail(),
                            "Approved volunteer registration",
                            processedHtmlContent);
                } else if (approveSubProgram.getValue().equals("Reject") && subProgram.getType().equals("volunteer")) {
                    Resource resource = new ClassPathResource("templates/RejectVolunteer.html");
                    String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                    String processedHtmlContent = htmlContent
                            .replace("[[Recipient_Name]]", subProgram.getUser().getDisplayName())
                            .replace("[[Note]]", subProgram.getNote())
                            .replace("[[Program_Name]]", subProgram.getProgram().getProgramName());
                    sendEmailService.sendEmail(subProgram.getUser().getEmail(),
                            "Volunteer Registration Declined",
                            processedHtmlContent);
                }
                return ResponseEntity.ok().body(approveSubProgram.getValue()+" subprogram success");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found subprogram");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<?> getAllByUser(HttpServletRequest request, PaginateAndSearchByNameDto paginateDto) {
        try {
            PaginateAndSearchByNameDto paginate = new PaginateAndSearchByNameDto(paginateDto.getPage(), paginateDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(paginate.getPage()-1, paginate.getSize(),sort);
            UserEntity user = userService.findUserByToken(request);
            Page<SubProgramEntity> subProgramEntityPage = repository.findAllByUser(user,pageRequest);
            if(subProgramEntityPage.isEmpty()){
                return ResponseEntity.ok().body(new ArrayList<>());
            }
            List<SubProgramDto> subProgramDtoList = new ArrayList<>();
            for(SubProgramEntity subProgram : subProgramEntityPage.getContent()){
                GetMeDto gm = EntityDtoConverter.convertToDto(subProgram.getUser(), GetMeDto.class);
                ProgramDto programDto = EntityDtoConverter.convertToDto(subProgram.getProgram(), ProgramDto.class);
                SubProgramDto subProgramDto = EntityDtoConverter.convertToDto(subProgram, SubProgramDto.class);
                subProgramDto.setUser(gm);
                subProgramDto.setProgram(programDto);
                subProgramDtoList.add(subProgramDto);
            }
            return ResponseEntity.ok().body(subProgramDtoList);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllByProgramAndStatus(Long programId, PaginateAndSearchByNameDto paginateDto) {
        try {
            PaginateAndSearchByNameDto paginate = new PaginateAndSearchByNameDto(paginateDto.getPage(), paginateDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(paginate.getPage()-1, paginate.getSize(),sort);
            ProgramEntity program = programService.findById(programId);
            Page<SubProgramEntity> subProgramEntityPage = repository.findAllByProgramAndStatus(program, paginateDto.getName(), pageRequest);
            if(subProgramEntityPage.isEmpty()){
                return ResponseEntity.ok().body(new ArrayList<>());
            }
            List<SubProgramDto> subProgramDtoList = new ArrayList<>();
            for(SubProgramEntity subProgram : subProgramEntityPage.getContent()){
                GetMeDto gm = EntityDtoConverter.convertToDto(subProgram.getUser(), GetMeDto.class);
                ProgramDto programDto = EntityDtoConverter.convertToDto(subProgram.getProgram(), ProgramDto.class);
                SubProgramDto subProgramDto = EntityDtoConverter.convertToDto(subProgram, SubProgramDto.class);
                subProgramDto.setUser(gm);
                subProgramDto.setProgram(programDto);
                subProgramDtoList.add(subProgramDto);
            }
            return ResponseEntity.ok().body(subProgramDtoList);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getByAllField(String search) {
       try{
           Specification<SubProgramEntity> spec = new DynamicSpecification<>(search);
           List<SubProgramEntity> subProgramEntityList = repository.findAll(spec);
           if (subProgramEntityList.isEmpty()) {
               return ResponseEntity.ok().body(new ArrayList<>());
           }
           List<SubProgramDto> subProgramDtoList = new ArrayList<>();
           for(SubProgramEntity subProgram : subProgramEntityList){
               GetMeDto gm = EntityDtoConverter.convertToDto(subProgram.getUser(), GetMeDto.class);
               ProgramDto programDto = EntityDtoConverter.convertToDto(subProgram.getProgram(), ProgramDto.class);
               SubProgramDto subProgramDto = EntityDtoConverter.convertToDto(subProgram, SubProgramDto.class);
               subProgramDto.setUser(gm);
               subProgramDto.setProgram(programDto);
               subProgramDtoList.add(subProgramDto);
           }
           return ResponseEntity.ok().body(subProgramDtoList);
       }catch (Exception e){
           System.out.println(e.getMessage());
           return ResponseEntity.internalServerError().body(e.getMessage());
       }
    }

    @Override
    public SubProgramEntity getByUserAndProgram(UserEntity user, ProgramEntity program,String type) {
        try {
            return repository.findByUserAndProgramAndType(user,program,type);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<SubProgramEntity> getAllByProgramAndStatus(ProgramEntity program, String status) {
        try{
            return repository.findAllByProgramAndStatus(program,status,null).getContent();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
