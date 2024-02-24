package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.partner.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partner.PartnerDto;
import com.example.beprojectsem4.dtos.partner.UpdatePartnerDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void createPartner(@Nullable List<MultipartFile> files, CreatePartnerDto createPartnerDto) {
        try {
            if(!checkPartnertByEmail(createPartnerDto.getEmail()) && !checkPartnertByPartnerName(createPartnerDto.getPartnerName())){
                PartnerEntity partner = EntityDtoConverter.convertToEntity(createPartnerDto, PartnerEntity.class);
                partner.setStatus("Active");
                PartnerEntity pn = partnerRepository.save(partner);
                List<String> urls = imageUploadService.imageUpload(files);
                for(String url : urls){
                    PartnerAttachmentEntity attachment = new PartnerAttachmentEntity(pn,"Logo",url);
                    attachmentRepository.save(attachment);
                }

            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<PartnerDto> listPartner() {
        try {
            List<PartnerEntity> partners = partnerRepository.findAll();
            List<PartnerDto> partnerDtos = new ArrayList<>();
            for (PartnerEntity p : partners){
                PartnerDto pd = EntityDtoConverter.convertToDto(p,PartnerDto.class);
                partnerDtos.add(pd);
            }
            return partnerDtos;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }


    @Override
    public void updatepartner(@Nullable List<MultipartFile> files,Long id, UpdatePartnerDto updatePartnerDto) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if (partner.isPresent()) {
                PartnerEntity p = EntityDtoConverter.convertToEntity(updatePartnerDto,PartnerEntity.class);
                PartnerEntity pn = TransferValuesIfNull.transferValuesIfNull(partner.get(),p);
                partnerRepository.save(pn);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void blockPartner(Long id) {
        try {
            Optional<PartnerEntity> partner = partnerRepository.findById(id);
            if (partner.isPresent()) {
                PartnerEntity p = partner.get();
                p.setStatus("Blocked");
                partnerRepository.save(p);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
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
    public List<PartnerEntity> searchAllField(String value) {
        try {
            Specification<PartnerEntity> spec = new DynamicSpecification<>(value);
            return partnerRepository.findAll(spec);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
