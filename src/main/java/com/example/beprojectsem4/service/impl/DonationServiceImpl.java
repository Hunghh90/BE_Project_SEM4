package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.exception.NotFoundException;
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
	public DonationEntity FindByUser(UserEntity entity) {
		return donationRepository.FindByUser(entity);
	}
    
}
