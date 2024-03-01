package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.GetPartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    void createPartner( CreatePartnerDto createPartnerDto);
    PartnerEntity getPartner(Long id);
    List<PartnerDto> listPartner(GetPartnerDto getPartnerDto);
    void updatePartner(Long id,UpdatePartnerDto updatePartnerDto);
    void blockPartner(Long id);
    boolean checkPartnertByEmail(String email);
    boolean checkPartnertByPartnerName(String partnerName);

    List<PartnerEntity> searchAllField(String value);
}
