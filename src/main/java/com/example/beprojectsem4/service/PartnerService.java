package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.partner.CreatePartnerDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerService {

    void createPartner(List<MultipartFile> files, CreatePartnerDto createPartnerDto);

    List<PartnerEntity> listPartner();
    PartnerEntity getPartner(String search);
    void updatepartner();
    void blockPartner();
    boolean checkPartnertByEmail(String email);
    boolean checkPartnertByPartnerName(String partnerName);
}
