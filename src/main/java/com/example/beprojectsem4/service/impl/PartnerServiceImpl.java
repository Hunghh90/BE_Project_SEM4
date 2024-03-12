package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.GetPartnersDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.entities.PartnerAttachmentEntity;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.RoleEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.DynamicSpecification;
import com.example.beprojectsem4.repository.PartnerAttachmentRepository;
import com.example.beprojectsem4.repository.PartnerRepository;
import com.example.beprojectsem4.repository.RoleRepository;
import com.example.beprojectsem4.service.ImageUploadService;
import com.example.beprojectsem4.service.PartnerService;
import com.example.beprojectsem4.service.RoleService;
import com.example.beprojectsem4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public ResponseEntity<?> createPartner(CreatePartnerDto createPartnerDto) {
        try {
            if(!checkPartnertByEmail(createPartnerDto.getEmail()) && !checkPartnertByPartnerName(createPartnerDto.getPartnerName())){
                PartnerEntity partner = EntityDtoConverter.convertToEntity(createPartnerDto, PartnerEntity.class);
                partner.setStatus("Active");
                UserEntity user = userService.checkUser(createPartnerDto.getEmail());
                if(user == null){
                    RegisterDto createUser = new RegisterDto(
                            createPartnerDto.getEmail(),
                            "12345678",
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
                    return ResponseEntity.status(HttpStatus.CREATED).body("Create partner success");
            }
            return ResponseEntity.badRequest().body("Partner name or Email is already");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> listPartner(GetPartnersDto getPartnerDto) {
        try {
            if(getPartnerDto.getPage() <=0){
                getPartnerDto.setPage(1);
            }
            if(getPartnerDto.getSize()<=0){
                getPartnerDto.setSize(20);
            }
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(getPartnerDto.getPage()-1, getPartnerDto.getSize(),sort);
            Page<PartnerEntity> partners = partnerRepository.findByPartnerNameContaining(getPartnerDto.getPartnerName(),pageRequest);
            if (partners.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No found partner");
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


    @Override
    public ResponseEntity<?> updatePartner(Long id, UpdatePartnerDto updatePartnerDto) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if (partner.isPresent()) {
                PartnerEntity p = EntityDtoConverter.convertToEntity(updatePartnerDto,PartnerEntity.class);
                PartnerEntity pn = TransferValuesIfNull.transferValuesIfNull(partner.get(),p);
                partnerRepository.save(pn);
                return ResponseEntity.ok().body("Update success");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> blockPartner(Long id) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if (partner.isPresent()) {
                PartnerEntity p = partner.get();
                p.setStatus("Blocked");
                partnerRepository.save(p);
                return ResponseEntity.ok().body("Block success");
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
                PartnerDto partnerDto = EntityDtoConverter.convertToDto(partner.get(),PartnerDto.class);
                return ResponseEntity.ok().body(partnerDto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found partner");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
