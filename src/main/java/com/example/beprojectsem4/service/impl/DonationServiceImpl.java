package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.Donation.DonateDto;
import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.exception.NotFoundException;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.DonationRepository;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DonationServiceImpl implements DonationService {
    @Autowired
    ProgramRepository programRepository;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    UserService userService;

    @Override
    public void DonationSuccess(HttpServletRequest request,DonateDto donateDto) {
        try{
            Optional<ProgramEntity> programEntityOptional =  programRepository.findById(donateDto.getId());
            if(programEntityOptional.isEmpty()){
                throw new NotFoundException("Not found Program");
            }
            ProgramEntity programEntity = programEntityOptional.get();
            programEntity.setTotalMoney(programEntity.getTotalMoney() + donateDto.getAmount());
            programRepository.save(programEntity);
            DonationEntity donate = EntityDtoConverter.convertToEntity(donateDto,DonationEntity.class);
            UserEntity user = userService.findUserByToken(request);
            if(user != null){
                donate.setUser(user);
                donate.setProgram(programEntity);
            }
            donationRepository.save(donate);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
