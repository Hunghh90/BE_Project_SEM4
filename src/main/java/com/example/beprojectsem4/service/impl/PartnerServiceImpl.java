package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.dtos.programDtos.DetailProgramDto;
import com.example.beprojectsem4.entities.*;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.DynamicSpecification;
import com.example.beprojectsem4.repository.PartnerAttachmentRepository;
import com.example.beprojectsem4.repository.PartnerRepository;
import com.example.beprojectsem4.service.*;
import org.apache.commons.lang3.RandomStringUtils;
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
public class PartnerServiceImpl implements PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private PartnerAttachmentRepository attachmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SendEmailService sendEmailService;
    private final ProgramService programService;
    @Autowired
    public PartnerServiceImpl(@Lazy ProgramService programService){
        this.programService = programService;
    };

 @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> getPartnerByEmail(String email) {
        try{
            PartnerEntity partner = partnerRepository.findByEmail(email);
            if(partner !=null){
                return ResponseEntity.ok().body(partner);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> createPartner(CreatePartnerDto createPartnerDto) {
        try {
            if(!checkPartnertByEmail(createPartnerDto.getEmail()) && !checkPartnertByPartnerName(createPartnerDto.getPartnerName())){
                PartnerEntity partner = EntityDtoConverter.convertToEntity(createPartnerDto, PartnerEntity.class);
                partner.setStatus("Active");
                UserEntity user = userService.checkUser(createPartnerDto.getEmail());
                String password = RandomStringUtils.random(8, true, true);
                if(user == null){
                    RegisterDto createUser = new RegisterDto(
                            createPartnerDto.getEmail(),
                            password,
                            null,
                            null,
                            createPartnerDto.getPartnerName(),
                            createPartnerDto.getUrlLogo()
                    );
                    userService.createAccountPartner(createUser);
                }else{
                    RoleEntity userRole = roleService.findRoleByRoleName("PARTNER");
                    if (userRole == null) {
                        roleService.createRole("PARTNER");
                        userRole = roleService.findRoleByRoleName("PARTNER");
                    }
                    user.getRoles().clear();
                    user.getRoles().add(userRole);
                }
                PartnerEntity pn = partnerRepository.save(partner);
                    PartnerAttachmentEntity attachment = new PartnerAttachmentEntity(pn,"Logo", createPartnerDto.getUrlLogo());
                    attachmentRepository.save(attachment);
                Resource resource = new ClassPathResource("templates/CreateAccountPartner.html");
                String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
                String processedHtmlContent = htmlContent
                        .replace("[[Recipient_Name]]", pn.getPartnerName())
                        .replace("[[Partner_Account]]", pn.getEmail())
                        .replace("[[Password]]", password);
                sendEmailService.sendEmail(pn.getEmail(),
                        "Confirmation of Approval and Account Creation for Your Charity ",
                        processedHtmlContent);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Create partner success");
            }
            return ResponseEntity.badRequest().body("Partner name or Email is already");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> listPartner(PaginateAndSearchByNameDto paginateDto) {
        try {
            PaginateAndSearchByNameDto paginateAndSearchByNameDto = new PaginateAndSearchByNameDto(paginateDto.getPage(), paginateDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(paginateAndSearchByNameDto.getPage()-1, paginateAndSearchByNameDto.getSize(),sort);
            Page<PartnerEntity> partners = partnerRepository.findByPartnerNameContaining(paginateDto.getName(),pageRequest);
            if (partners.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
            }
            List<PartnerDto> partnerDto = new ArrayList<>();
            for (PartnerEntity p : partners){
                PartnerDto pd = EntityDtoConverter.convertToDto(p,PartnerDto.class);
                partnerDto.add(pd);
            }
            return ResponseEntity.ok().body(partnerDto);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('PARTNER')")
    @Override
    public ResponseEntity<?> updatePartner(Long id, UpdatePartnerDto updatePartnerDto) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if (partner.isPresent()) {
                PartnerEntity p = EntityDtoConverter.convertToEntity(updatePartnerDto,PartnerEntity.class);
                PartnerEntity pn = TransferValuesIfNull.transferValuesIfNull(partner.get(),p);
                partnerRepository.save(pn);
                PartnerAttachmentEntity attachment = new PartnerAttachmentEntity(pn,"Logo", updatePartnerDto.getUrlLogo());
                attachmentRepository.save(attachment);
                return ResponseEntity.ok().body("Update success");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> toggleLockPartner(Long id,String value) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if (partner.isPresent()) {
                PartnerEntity p = partner.get();
                if(value.equals("Block")){
                    userService.toggleLockUser(partner.get().getEmail(),value);
                    for(ProgramEntity program : p.getPrograms()){
                        programService.toggleLockProgram(program.getProgramId(),value);
                    }
                }
                if(value.equals("Active")){
                    userService.toggleLockUser(partner.get().getEmail(),value);
                }
                    for(ProgramEntity program : p.getPrograms()){
                        programService.toggleLockProgram(program.getProgramId(),value);
                }
                p.setStatus(value);
                partnerRepository.save(p);
                return ResponseEntity.ok().body(value+" success");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    @Override
    public boolean checkPartnertByEmail(String email) {
        try{
            PartnerEntity partner = partnerRepository.findByEmail(email);
            if(partner == null) {
                return false;
            }
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkPartnertByPartnerName(String partnerName) {
        try{
            PartnerEntity partner = partnerRepository.findByPartnerName(partnerName);
            if(partner == null) {
                return false;
            }
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> searchAllField(String value) {
        try {
            Specification<PartnerEntity> spec = new DynamicSpecification<>(value);
            List<PartnerEntity> partners = partnerRepository.findAll(spec);
            if (partners.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No partners found for the given search criteria");
            }
            List<PartnerDto> partnerDtoList = new ArrayList<>();
            for(PartnerEntity p:partners){
                PartnerDto partnerDto = EntityDtoConverter.convertToDto(p,PartnerDto.class);
                partnerDtoList.add(partnerDto);
            }
            return ResponseEntity.ok().body(partnerDtoList);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getPartner(Long id) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if(partner.isPresent()){
                List<DetailProgramDto> total = new ArrayList<>();

                for (ProgramEntity program : partner.get().getPrograms()){
                    Map<String, Double> totalAmountByPaymentMethod = new HashMap<>();
                    for (DonationEntity donation : program.getDonations()) {
                        String paymentMethod = donation.getPaymentMethod();
                        Double amount = donation.getAmount();
                        totalAmountByPaymentMethod.put(paymentMethod, totalAmountByPaymentMethod.getOrDefault(paymentMethod, 0.0) + amount);
                    }
                    DetailProgramDto moneyDto = EntityDtoConverter.convertToDto(program, DetailProgramDto.class);
                    moneyDto.setDonateByPaypal(totalAmountByPaymentMethod.getOrDefault("Paypal",0.0));
                    moneyDto.setDonateByVNPay(totalAmountByPaymentMethod.getOrDefault("VNPay",0.0));
                    total.add(moneyDto);
                }
                PartnerDto partnerDto = EntityDtoConverter.convertToDto(partner.get(),PartnerDto.class);
                partnerDto.setPrograms(total);
                return ResponseEntity.ok().body(partnerDto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
