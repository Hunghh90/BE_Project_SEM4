package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.partner.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partner.PartnerDto;
import com.example.beprojectsem4.dtos.partner.UpdatePartnerDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerService {

    void createPartner(@Nullable List<MultipartFile> files, CreatePartnerDto createPartnerDto);

    List<PartnerDto> listPartner();
    void updatepartner(@Nullable List<MultipartFile> files,Long id,UpdatePartnerDto updatePartnerDto);
    void blockPartner(Long id);
    boolean checkPartnertByEmail(String email);
    boolean checkPartnertByPartnerName(String partnerName);

    List<PartnerEntity> searchAllField(String value);
}
