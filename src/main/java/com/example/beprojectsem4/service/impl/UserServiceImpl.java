package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.ChangePassword;
import com.example.beprojectsem4.dtos.RegisterDto;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.UserService;
import com.example.beprojectsem4.service.jwt.JwtAuthenticationFilter;
import com.example.beprojectsem4.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository repository;
    @Override
    public ResponseEntity<?> createAccount(RegisterDto registerDto) {
        try {
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto,UserEntity.class);
            user.setStatus("Deactivate");
            repository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Create success");
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred");
        }
    }

    @Override
    public ResponseEntity<?> changePassword(HttpServletRequest request,ChangePassword changePassword) {
        try{
            String token = jwtAuthenticationFilter.getToken(request);
            UserEntity user = jwtService.getMeFromToken(token);
            boolean checkPassword = bCryptPasswordEncoder.matches(changePassword.getOldPassword(),user.getPassword());
            if(!checkPassword){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password not correct");
            }
            if(!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())){
                return ResponseEntity.status(HttpStatus.OK).body("Confirm password not correct");

            }
            user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
            repository.save(user);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password not success "+ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password success");
    }

    public UserEntity findUserByEmail(String email){
        return repository.findUserByEmail(email);
    }
}
