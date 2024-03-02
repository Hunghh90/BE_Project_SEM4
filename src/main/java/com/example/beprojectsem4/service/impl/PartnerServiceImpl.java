package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.GetPartnersDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.entities.PartnerAttachmentEntity;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.DynamicSpecification;
import com.example.beprojectsem4.repository.PartnerAttachmentRepository;
import com.example.beprojectsem4.repository.PartnerRepository;
import com.example.beprojectsem4.service.ImageUploadService;
import com.example.beprojectsem4.service.PartnerService;
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
    private ImageUploadService imageUploadService;
    @Autowired
    private PartnerAttachmentRepository attachmentRepository;
    @Override
    public ResponseEntity<?> createPartner(CreatePartnerDto createPartnerDto) {
        try {
            if(!checkPartnertByEmail(createPartnerDto.getEmail()) && !checkPartnertByPartnerName(createPartnerDto.getPartnerName())){
                PartnerEntity partner = EntityDtoConverter.convertToEntity(createPartnerDto, PartnerEntity.class);
                partner.setStatus("Active");
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
            List<PartnerDto> partnerDtos = new ArrayList<>();
            for (PartnerEntity p : partners){
                PartnerDto pd = EntityDtoConverter.convertToDto(p,PartnerDto.class);
                partnerDtos.add(pd);
            }
            return ResponseEntity.ok().body(partnerDtos);
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
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return null;
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
            return ResponseEntity.badRequest().body("Not found partner");
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
            return ResponseEntity.ok().body(partners);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getPartner(Long id) {
        try {
            PartnerEntity partner = partnerRepository.findById(id).get();
            return ResponseEntity.ok().body(partner);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
