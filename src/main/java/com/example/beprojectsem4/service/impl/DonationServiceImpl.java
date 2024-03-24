package com.example.beprojectsem4.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.exception.NotFoundException;
import com.example.beprojectsem4.repository.DonationRepository;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.UserService;

@Service
public class DonationServiceImpl implements DonationService {
    @Autowired
    ProgramRepository programRepository;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    UserService userService;

    @Override
    public void DonationSuccess(Long id, double amount,String paymentMethod) {
        Optional<ProgramEntity> programEntityOptional =  programRepository.findById(id);
        if(!programEntityOptional.isPresent()){
            throw new NotFoundException("Not found Program");
        }
        ProgramEntity programEntity = programEntityOptional.get();
        programEntity.setTotalMoney(programEntity.getTotalMoney() + amount);
        programRepository.save(programEntity);

    }

	@Override
	public DonationEntity FindByUserAndProgram(UserEntity userEntity, ProgramEntity programEntity) {
		
		return donationRepository.findByUserAndProgram(userEntity, programEntity);
	}

    
}
