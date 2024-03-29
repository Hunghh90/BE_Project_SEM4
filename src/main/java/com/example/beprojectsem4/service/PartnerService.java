package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.GetListPartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import org.springframework.http.ResponseEntity;

public interface PartnerService {

    ResponseEntity<?> createPartner(CreatePartnerDto createPartnerDto);
    ResponseEntity<?> getPartner(Long id);
    ResponseEntity<?> getPartnerByEmail(String email);
    ResponseEntity<?> listPartner(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
    ResponseEntity<?> updatePartner(Long id, UpdatePartnerDto updatePartnerDto);
    ResponseEntity<?> toggleLockPartner(Long id,String value);
    boolean checkPartnertByEmail(String email);
    boolean checkPartnertByPartnerName(String partnerName);

    ResponseEntity<?> searchAllField(String value);
}
