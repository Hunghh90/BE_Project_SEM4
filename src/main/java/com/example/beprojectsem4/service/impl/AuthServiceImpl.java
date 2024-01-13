package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.AuthService;
import com.example.beprojectsem4.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private JwtService jwtService;
    @Autowired
    private UserRepository repository;
    @Override
    public String register(UserEntity user) {
        try{
            UserEntity u = checkUser(user.getEmail());
            if(u != null){
                return "User exist";
            }
            repository.save(user);
            return "Success";
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }
    }

    @Override
    public String login(String email, String password) {
        return null;
    }

    public UserEntity checkUser(String email){
        UserEntity u = null;
        try{
            u = repository.findUserByEmail(email);
            if (u != null) {
                return u;
            } else {
                return null;
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
