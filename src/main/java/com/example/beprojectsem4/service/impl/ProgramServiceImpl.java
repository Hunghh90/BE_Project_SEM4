package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.ProgramAttachmentRepository;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.example.beprojectsem4.service.PartnerService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramAttachmentRepository programAttachmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PartnerService partnerService;
    @Override
    public void createProgram(HttpServletRequest request,CreateProgramDto createProgramDto) {

        try{
            GetMeDto me = userService.getMe(request);
            UserEntity m = EntityDtoConverter.convertToEntity(me,UserEntity.class);
            PartnerEntity pn = partnerService.getPartner(createProgramDto.partnerId);
            if(!checkProgramByProgramName(createProgramDto.getProgramName())){
                ProgramEntity program = EntityDtoConverter.convertToEntity(createProgramDto,ProgramEntity.class);
                program.setStatus("Active");
                program.setUser(m);
                program.setPartner(pn);
                ProgramEntity pr = programRepository.save(program);
                for (String url : createProgramDto.getImageUrl()){
                    ProgramAttachmentEntity pa = new ProgramAttachmentEntity(pr,"Certify",url);
                    programAttachmentRepository.save(pa);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<ProgramEntity> listProgram() {
        try{
            List<ProgramEntity> programs = programRepository.findAll();
            return programs;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void updateProgram(HttpServletRequest request, Long id, UpdateProgramDto updateProgramDto) {
        Date now = new Date();
        try {
            GetMeDto me = userService.getMe(request);
            UserEntity m = EntityDtoConverter.convertToEntity(me,UserEntity.class);
            Optional<ProgramEntity> pr = programRepository.findById(id);
            if(pr.isPresent() && pr.get().getStartDonateDate().before(now)){
                ProgramEntity updateProgram = EntityDtoConverter.convertToEntity(updateProgramDto,ProgramEntity.class);
                ProgramEntity up = TransferValuesIfNull.transferValuesIfNull(pr.get(),updateProgram);
                up.setUser(m);
                programRepository.save(up);
                if(updateProgramDto.getImageUrl() != null && !updateProgramDto.getImageUrl().isEmpty()){
                    List<ProgramAttachmentEntity> pa = programAttachmentRepository.findByProgramId_programId(pr.get().getProgramId());
                    programAttachmentRepository.deleteAll(pa);
                    for (String url : updateProgramDto.getImageUrl()){
                        ProgramAttachmentEntity img = new ProgramAttachmentEntity(pr.get(),"Certify",url);
                        programAttachmentRepository.save(img);
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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
