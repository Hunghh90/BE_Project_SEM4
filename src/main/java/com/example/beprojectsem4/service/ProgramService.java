package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProgramService {
    void createProgram(HttpServletRequest request, CreateProgramDto createProgramDto);

    List<ProgramEntity> listProgram();
    void updateProgram(HttpServletRequest request,Long id, UpdateProgramDto updateProgramDto);
    void blockProgram(Long id);
    boolean checkProgramByProgramName(String programName);

    List<ProgramEntity> searchAllField(String value);
}
