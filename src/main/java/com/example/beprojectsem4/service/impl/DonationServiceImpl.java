package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.DonationRepository;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DonationServiceImpl implements DonationService {
    @Autowired
    private ProgramService programService;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<?> DonationSuccess(HttpServletRequest request, CreateDonateDto donateDto) {
        try{
            ProgramEntity programEntity = programService.addMoneyDonate(donateDto);
            if(programEntity != null){
                DonationEntity donate = EntityDtoConverter.convertToEntity(donateDto,DonationEntity.class);
                UserEntity user = userService.findUserByToken(request);
                if(user != null){
                    donate.setUser(user);
                    donate.setProgram(programEntity);
                }
                donationRepository.save(donate);
                return ResponseEntity.ok().body("Success");
            }else {
                return ResponseEntity.badRequest().body("Not success");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
