package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.RegisterDto;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Override
    public boolean createAccount(RegisterDto registerDto) {
        try {
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto,UserEntity.class);
            user.setStatus("Deactivate");
            repository.save(user);
            return true;
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    public UserEntity findUserByEmail(String email){
        return repository.findUserByEmail(email);
    }
}
