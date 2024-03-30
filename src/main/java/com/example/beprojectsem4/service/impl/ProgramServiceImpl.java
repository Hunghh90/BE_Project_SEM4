package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.donationDtos.CreateDonateDto;
import com.example.beprojectsem4.dtos.donationDtos.DonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.*;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.entities.*;
import com.example.beprojectsem4.exception.NotFoundException;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.DynamicSpecification;
import com.example.beprojectsem4.repository.ProgramAttachmentRepository;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.example.beprojectsem4.repository.SubProgramRepository;
import com.example.beprojectsem4.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramAttachmentRepository programAttachmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private SendEmailService sendEmailService;

    private final SubProgramService subProgramService;
    @Autowired
    public ProgramServiceImpl(@Lazy SubProgramService subProgramService){
        this.subProgramService = subProgramService;
    }
    @PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto) {

        try {
            UserEntity user = userService.findUserByToken(request);
            PartnerEntity pn = (PartnerEntity) partnerService.getPartnerByEmail(user.getEmail()).getBody();
            if (!checkProgramByProgramName(createProgramDto.getProgramName()) && createProgramDto.getStartDonateDate().before(createProgramDto.getEndDonateDate()) && createProgramDto.getEndDonateDate().before(createProgramDto.getFinishDate())) {
                String escapedData = StringEscapeUtils.escapeHtml4(createProgramDto.getDescription());
                ProgramEntity program = EntityDtoConverter.convertToEntity(createProgramDto, ProgramEntity.class);
                program.setStatus("DeActive");
                program.setUser(user);
                program.setPartner(pn);
                program.setDescription(escapedData);
                ProgramEntity pr = programRepository.save(program);
                for (String url : createProgramDto.getImageUrl()) {
                    ProgramAttachmentEntity pa = new ProgramAttachmentEntity(pr, "Certify", url);
                    programAttachmentRepository.save(pa);
                }
                ProgramAttachmentEntity lg = new ProgramAttachmentEntity(pr, "Logo", createProgramDto.getImageLogo());
                programAttachmentRepository.save(lg);
                return ResponseEntity.ok().body("Create program success");
            }
            return ResponseEntity.badRequest().body("Program name is already or The date entered is incorrect");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("isAnonymous()")
    @Override
    public ResponseEntity<?> listProgram(PaginateAndSearchByNameDto paginateDto) {
        try {
            PaginateAndSearchByNameDto paginateAndSearchByNameDto = new PaginateAndSearchByNameDto( paginateDto.getPage(), paginateDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageable = PageRequest.of(paginateAndSearchByNameDto.getPage() - 1, paginateAndSearchByNameDto.getSize(), sort);
            Page<ProgramEntity> programs = programRepository.findByProgramNameContaining(paginateDto.getName(), pageable);
            List<ProgramDto> programDtoList = convertToProgramDto(programs.getContent());
            return ResponseEntity.ok().body(programDtoList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> updateProgram(Long id, UpdateProgramDto updateProgramDto) {
        Date now = new Date();
        try {
            ProgramEntity program = findById(id);
            assert program != null;
            if (program.getStartDonateDate().before(now)) {
                ProgramEntity updateProgram = EntityDtoConverter.convertToEntity(updateProgramDto, ProgramEntity.class);
                if (!updateProgram.getDescription().isEmpty()) {
                    String escapedData = StringEscapeUtils.escapeHtml4(updateProgramDto.getDescription());
                    program.setDescription(escapedData);
                }
                ProgramEntity up = TransferValuesIfNull.transferValuesIfNull(program, updateProgram);
                if(program.getStatus().equals("Active")){
                    program.setStatus("DeActive");
                }
                if (updateProgramDto.getImageUrl() != null && !updateProgramDto.getImageUrl().isEmpty()) {
                    for (String url : updateProgramDto.getImageUrl()) {
                        ProgramAttachmentEntity existingImage = programAttachmentRepository.findByUrlAndTypeAndProgramId_programId("Certify", url, program.getProgramId());
                        if (existingImage == null) {
                            ProgramAttachmentEntity img = new ProgramAttachmentEntity(program, "Certify", url);
                            programAttachmentRepository.save(img);
                        } else {
                            existingImage.setUrl(url);
                            programAttachmentRepository.save(existingImage);
                        }
                    }
                }
                programRepository.save(up);

                if (updateProgramDto.getImageLogo() != null && !updateProgramDto.getImageLogo().isEmpty()) {
                    ProgramAttachmentEntity existingImage = programAttachmentRepository.findByUrlAndTypeAndProgramId_programId("Logo", updateProgramDto.getImageLogo(), program.getProgramId());
                    if (existingImage == null) {
                        ProgramAttachmentEntity img = new ProgramAttachmentEntity(program, "Logo", updateProgramDto.getImageLogo());
                        programAttachmentRepository.save(img);
                    } else {
                        existingImage.setUrl(updateProgramDto.getImageLogo());
                        programAttachmentRepository.save(existingImage);
                    }
                }
                return ResponseEntity.ok().body("Update program success");
            }

            return ResponseEntity.badRequest().body("Program not exists or Update timed out");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> approveProgram(Long id, RejectProgramDto rejectProgramDto) {
        try {
            ProgramEntity program = findById(id);
            if (program != null &&
                    (program.getStatus().equals("DeActive") || program.getStatus().equals("Block"))) {
                program.setStatus(rejectProgramDto.getValue());
                if (rejectProgramDto.getValue().equals("Active")) {
                    program.setTotalMoney(0.0);
                    Resource resource = new ClassPathResource("templates/ApproveProgram.html");
                        String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                        String processedHtmlContent = htmlContent
                                .replace("[[Recipient_Name]]",program.getPartner().getPartnerName())
                                .replace("[[Program_Name]]", program.getProgramName());
                        sendEmailService.sendEmail(program.getPartner().getEmail(),
                                "Notification Regarding Approval Status of Your Charity Program : " + program.getProgramName(),
                                processedHtmlContent);
                } else {
                    program.setReasonRejection(rejectProgramDto.getReasonRejection());
                    Resource resource = new ClassPathResource("templates/RejectProgram.html");
                    String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                    String processedHtmlContent = htmlContent
                            .replace("[[Recipient_Name]]",program.getPartner().getPartnerName())
                            .replace("[[Reason_Reject]]", program.getReasonRejection());
                    sendEmailService.sendEmail(program.getPartner().getEmail(),
                            "Notification Regarding Approval Status of Your Charity Program : " + program.getProgramName(),
                            processedHtmlContent);
                }
                program.setShare(0);
                programRepository.save(program);
                return ResponseEntity.ok().body(rejectProgramDto.getValue() + " program success");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found program");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> toggleLockProgram(Long id, String value) {
        try {
            ProgramEntity program = findById(id);
            if (program != null) {
                program.setStatus(value);
                programRepository.save(program);
                return ResponseEntity.ok().body(value + " program success");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found program");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @Override
    public boolean checkProgramByProgramName(String programName) {
        try {
            ProgramEntity program = programRepository.findByProgramName(programName);
            return program != null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    @Override
    public ResponseEntity<?> searchAllField(String value) {
        try {
            Specification<ProgramEntity> spec = new DynamicSpecification<>(value);
            List<ProgramEntity> programs = programRepository.findAll(spec);
            if (programs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No program found for the given search criteria");
            }
            List<ProgramDto> programDtoList = new ArrayList<>();
            for (ProgramEntity p : programs) {
                List<DonateDto> donateDtoList = listDonate(p);
                String normalData = StringEscapeUtils.unescapeHtml4(p.getDescription());
                p.setDescription(normalData);
                ProgramDto programDto = EntityDtoConverter.convertToDto(p, ProgramDto.class);
                programDto.setDonations(donateDtoList);
                programDtoList.add(programDto);
            }
            return ResponseEntity.ok().body(programDtoList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    @Override
    public ResponseEntity<?> listProgramByStatus(PaginateAndSearchByNameDto paginateDto) {
        try {
            PaginateAndSearchByNameDto paginateAndSearchByNameDto = new PaginateAndSearchByNameDto( paginateDto.getPage(), paginateDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageable = PageRequest.of(paginateAndSearchByNameDto.getPage() - 1, paginateAndSearchByNameDto.getSize(), sort);
            Page<ProgramEntity> programs = programRepository.findByStatus(paginateDto.getName(), pageable);
            List<ProgramDto> programDtoList = convertToProgramDto(programs.getContent());
            return ResponseEntity.ok().body(programDtoList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("isAnonymous()or isAuthenticated()")
    @Override
    public ResponseEntity<?> detailProgram(HttpServletRequest request,Long id) {
        try {
            ProgramEntity program = findById(id);
            if (program != null) {
                List<DonateDto> donateDtoList = listDonate(program);
                donateDtoList.sort(Comparator.comparing(DonateDto::getCreatedAt).reversed());
                String normalData = StringEscapeUtils.unescapeHtml4(program.getDescription());
                program.setDescription(normalData);
                ProgramDto programDto = EntityDtoConverter.convertToDto(program, ProgramDto.class);
                programDto.setDonations(donateDtoList);
                UserEntity user = userService.findUserByToken(request);
                if (user != null) {
                    SubProgramEntity subProgram = subProgramService.getByUserAndProgramAndType(user,program,"volunteer");
                    if(subProgram.getStatus().equals("Active")||subProgram.getStatus().equals("Pending")){
                        programDto.setVolunteer(true);

                    }else if(subProgram.getStatus().equals("Cancel")){
                        programDto.setVolunteer(false);
                    }

                }
                List<SubProgramEntity> subProgramEntityList = subProgramService.getAllByProgramAndStatus(program,"Active");
                programDto.setCountVolunteer(subProgramEntityList.size());
                return ResponseEntity.ok().body(programDto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found program");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ProgramEntity addMoneyDonate(CreateDonateDto donateDto) {
        try {
            ProgramEntity program = findById(donateDto.getProgramId());
            if (program == null) {
                throw new NotFoundException("Not found Program");
            }
            if (program.getStatus().equals("Active")) {
                program.setTotalMoney(program.getTotalMoney() + donateDto.getAmount());
                return programRepository.save(program);
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

//    @Scheduled(cron = "0 0 0 * * ?")
//    public void changeStatusProgram(){
//        try{
//            List<ProgramEntity> programEntityList = programRepository.findAll();
//            for(ProgramEntity program : programEntityList){
//                if(program.getStartDonateDate().before(new Date()) && program.getStatus().equals("Active")){
//                    program.setStatus("Coming soon");
//                } else if (program.getEndDonateDate().before(new Date()) && program.getStatus().equals("Coming soon")) {
//                    program.setStatus("Progress");
//                } else if (program.getStatus().equals("Progress") && program.getEndDonateDate().after(new Date()) ) {
//                    program.setStatus("End");
//                }
//            }
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
@PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> deleteCertify(ListUrlDto listUrlDto, Long id) {
        try {
            for (String url : listUrlDto.getUrl()) {
                ProgramAttachmentEntity image = programAttachmentRepository.findByUrlAndTypeAndProgramId_programId("Certify", url, id);
                if (image != null) {
                    programAttachmentRepository.delete(image);
                }
            }
            return ResponseEntity.status(202).body("delete success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> finishProgram(ListUrlDto listUrlDto, Long id) {
        try {
            ProgramEntity program = findById(id);
            if (program == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Program not found");
            }
            for (String url : listUrlDto.getUrl()) {
                ProgramAttachmentEntity pa = new ProgramAttachmentEntity(program, "finished", url);
                programAttachmentRepository.save(pa);
            }
            program.setStatus("End");
            programRepository.save(program);
            Resource resource = new ClassPathResource("templates/donateSuccess.html");
            String imageUrlJson = new ObjectMapper().writeValueAsString(listUrlDto.getUrl());
            for (DonationEntity donate : program.getDonations()){
               String email = donate.getUser().getEmail();
                String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                StringBuilder imagesHtml = new StringBuilder();
                for (String imageUrl : listUrlDto.getUrl()) {
                    imagesHtml.append("<img src='").append(imageUrl).append("' style='width: 100px; height: 100px;' />");
                }
                String processedHtmlContent = htmlContent
                        .replace("[[Recipient_Name]]", donate.getUser().getDisplayName())
                        .replace("[[Images]]", imagesHtml.toString());
                sendEmailService.sendEmail(email,
                        "Thank You for Your Support to Our Charity Program: " + program.getProgramName(),
                        processedHtmlContent);
            }
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ProgramEntity findById(Long id) {
        try {
            Optional<ProgramEntity> optionalProgramEntity = programRepository.findById(id);
            return optionalProgramEntity.orElse(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public List<DonateDto> listDonate(ProgramEntity program) {
        try {
            List<DonateDto> donateDtoList = new ArrayList<>();
            for (DonationEntity getDonate : program.getDonations()) {
                DonateDto donateDto = EntityDtoConverter.convertToDto(getDonate, DonateDto.class);
                GetMeDto gm = EntityDtoConverter.convertToDto(getDonate.getUser(), GetMeDto.class);
                gm.setDonations(null);
                donateDto.setUser(gm);
                donateDtoList.add(donateDto);
            }
            return donateDtoList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ProgramDto> convertToProgramDto(List<ProgramEntity> programs) {
        try {
            List<ProgramDto> programDtoList = new ArrayList<>();
            for (ProgramEntity p : programs) {
                List<DonateDto> donateDtoList = listDonate(p);
                String normalData = StringEscapeUtils.unescapeHtml4(p.getDescription());
                p.setDescription(normalData);
                ProgramDto pd = EntityDtoConverter.convertToDto(p, ProgramDto.class);
                pd.setDonations(donateDtoList);
                programDtoList.add(pd);
            }
            return programDtoList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> extendFinishProgram(Long id, UpdateProgramDto updateProgramDto) {
        try {
            ProgramEntity program = findById(id);
            assert program != null;
            if (updateProgramDto.getFinishDate() != null && updateProgramDto.getFinishDate().after(program.getFinishDate())) {
                program.setFinishDate(updateProgramDto.getFinishDate());
                return ResponseEntity.ok().body("Extend finish date success");
            }
            return ResponseEntity.badRequest().body("Extend finish date not success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> shareProgram(HttpServletRequest request, ShareProgramDto shareProgramDto) {
        try {
            UserEntity user = userService.findUserByToken(request);
            ProgramEntity program = findById(shareProgramDto.getProgramId());
            Resource resource = new ClassPathResource("templates/ShareProgram.html");
            for (String email : shareProgramDto.getEmails()) {
                String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                String processedHtmlContent = htmlContent
                        .replace("[[Recipient_Email]]", email)
                        .replace("[[Send_Name]]", user.getDisplayName())
                        .replace("[[Program_Name]]", program.getProgramName())
                        .replace("[[Url]]", shareProgramDto.getUrl());
                sendEmailService.sendEmail(email,
                        "Sharing Our Charity Program: " + program.getProgramName(),
                        processedHtmlContent);
            }
            int count = program.getShare();
            count = count +1;
            program.setShare(count);
            programRepository.save(program);
            return ResponseEntity.ok().body("Share program success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
