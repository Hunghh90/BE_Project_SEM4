package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.partner.CreatePartnerDto;
import com.example.beprojectsem4.entities.PartnerAttachmentEntity;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.PartnerAttachmentRepository;
import com.example.beprojectsem4.repository.PartnerRepository;
import com.example.beprojectsem4.service.ImageUploadService;
import com.example.beprojectsem4.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private PartnerAttachmentRepository attachmentRepository;
    @Override
    public void createPartner(List<MultipartFile> files, CreatePartnerDto createPartnerDto) {
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
    public List<PartnerEntity> listPartner() {
        try {
            List<PartnerEntity> partners = partnerRepository.findAll();
            return partners;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public PartnerEntity getPartner(String search) {
        return null;
    }

    @Override
    public void updatepartner() {

    }

    @Override
    public void blockPartner() {

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
}
