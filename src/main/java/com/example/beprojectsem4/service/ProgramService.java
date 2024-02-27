package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProgramService {
    void createPartner(@Nullable List<MultipartFile> files, CreatePartnerDto createPartnerDto);

    List<PartnerDto> listProgram();
    void updateProgram(@Nullable List<MultipartFile> files, Long id, UpdatePartnerDto updatePartnerDto);
    void blockProgram(Long id);
    boolean checkProgramByProgramName(String programName);

    List<ProgramEntity> searchAllField(String value);
}
