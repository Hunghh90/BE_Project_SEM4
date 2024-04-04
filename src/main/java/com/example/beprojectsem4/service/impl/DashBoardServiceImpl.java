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
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DashBoardServiceImpl implements DashboardSevice {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Override
    public ResponseEntity<?> getDashBoard(int year) {
        try {
            Map<Integer, Long> userCountsMap = userRepository.countUsersByMonth(year).stream()
                    .collect(Collectors.toMap(row -> (Integer) row[0], row -> (Long) row[1]));
            Map<Integer, Long> partnerCountsMap = partnerRepository.countPartnersByMonth(year).stream()
                    .collect(Collectors.toMap(row -> (Integer) row[0], row -> (Long) row[1]));
            Map<Integer, Long> programCountsMap = programRepository.countProgramsByMonth(year).stream()
                    .collect(Collectors.toMap(row -> (Integer) row[0], row -> (Long) row[1]));

            // Kết hợp dữ liệu từ các Map
            List<Map<String, Object>> combinedData = IntStream.rangeClosed(1, 12).mapToObj(month -> {
                Map<String, Object> monthData = new HashMap<>();
                String monthName = Month.of(month).name();
                monthData.put("month", monthName);
                monthData.put("user", Map.of("key", "user", "title", "User", "value", userCountsMap.getOrDefault(month, 0L)));
                monthData.put("partner", Map.of("key", "partner", "title", "Partner", "value", partnerCountsMap.getOrDefault(month, 0L)));
                monthData.put("program", Map.of("key", "program", "title", "Program", "value", programCountsMap.getOrDefault(month, 0L)));
                return monthData;
            }).collect(Collectors.toList());

            LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
            List<UserEntity> users = userRepository.findAll();
            List<PartnerEntity> partners = partnerRepository.findAll();
            List<ProgramEntity> programs = programRepository.findAll();
            int totalUserInMonth = 0;
            int totalPartnerInMonth = 0;
            int totalProgramInMonth = 0;
            int programPending = 0;
            int programActive = 0;
            int programFinish = 0;
            int totalPartner = 0;
            int totalUser = 0;
            for(UserEntity user : users){
                LocalDate createdAt = user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(user.getStatus().equals("Activate")){
                    totalUser +=1;
                }
                if(createdAt.isAfter(firstDayOfMonth) && createdAt.isBefore(lastDayOfMonth)){
                    totalUserInMonth +=1;
                }
            }
            for(PartnerEntity partner : partners){
                LocalDate createdAt = partner.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(partner.getStatus().equals("Active")){
                    totalPartner +=1;
                }
                if(createdAt.isAfter(firstDayOfMonth) && createdAt.isBefore(lastDayOfMonth)){
                    totalPartnerInMonth +=1;
                }
            }
            for(ProgramEntity program : programs){
                LocalDate createdAt = program.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(createdAt.isAfter(firstDayOfMonth) && createdAt.isBefore(lastDayOfMonth)){
                    totalProgramInMonth +=1;
                }
                if(program.getStatus().equals("Active")){
                    programActive +=1;
                } else if (program.getStatus().equals("End")) {
                    programFinish +=1;
                } else if (program.getStatus().equals("DeActive")) {
                    programPending +=1;
                }
            }
            GetDashBoardDto dashBoardDto = new GetDashBoardDto(programs.size(),totalProgramInMonth,totalPartner,totalPartnerInMonth,totalUser,totalUserInMonth,programActive,programFinish,programPending,combinedData);
            return ResponseEntity.ok().body(dashBoardDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
