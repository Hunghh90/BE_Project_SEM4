package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.service.ProgramService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Override
    public void createPartner(List<MultipartFile> files, CreatePartnerDto createPartnerDto) {
    }

    @Override
    public List<PartnerDto> listProgram() {
        return null;
    }

    @Override
    public void updateProgram(List<MultipartFile> files, Long id, UpdatePartnerDto updatePartnerDto) {

    }

    @Override
    public void blockProgram(Long id) {

    }

    @Override
    public boolean checkProgramByProgramName(String programName) {
        return false;
    }

    @Override
    public List<ProgramEntity> searchAllField(String value) {
        return null;
    }
}
