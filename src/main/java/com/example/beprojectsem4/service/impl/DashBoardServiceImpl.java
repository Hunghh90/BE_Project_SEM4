package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.dashboardDto.GetDashBoardDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.repository.PartnerRepository;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.DashboardSevice;
import com.example.beprojectsem4.service.PartnerService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashboardSevice {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Override
    public ResponseEntity<?> getDashBoard() {
        try {
            LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
            List<UserEntity> users = userRepository.findAll();
            List<PartnerEntity> partners = partnerRepository.findAll();
            List<ProgramEntity> programs = programRepository.findAll();
            int totalUserInMonth = 0;
            int totalPartnerInMonth = 0;
            int totalProgramInMonth = 0;
            for(UserEntity user : users){
                LocalDate createdAt = user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(createdAt.isAfter(firstDayOfMonth) && createdAt.isBefore(lastDayOfMonth)){
                    totalUserInMonth +=1;
                }
            }
            for(PartnerEntity partner : partners){
                LocalDate createdAt = partner.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(createdAt.isAfter(firstDayOfMonth) && createdAt.isBefore(lastDayOfMonth)){
                    totalPartnerInMonth +=1;
                }
            }
            for(ProgramEntity program : programs){
                LocalDate createdAt = program.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(createdAt.isAfter(firstDayOfMonth) && createdAt.isBefore(lastDayOfMonth)){
                    totalProgramInMonth +=1;
                }
            }
            GetDashBoardDto dashBoardDto = new GetDashBoardDto(programs.size(),totalProgramInMonth,partners.size(),totalPartnerInMonth,users.size(),totalUserInMonth);
            return ResponseEntity.ok().body(dashBoardDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
