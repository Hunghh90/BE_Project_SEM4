package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.JwtResponseDto;
import com.example.beprojectsem4.dtos.LoginDto;
import com.example.beprojectsem4.dtos.RegisterDto;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.service.AuthService;
import com.example.beprojectsem4.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserServiceImpl service;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        try{
            UserEntity u = checkUser(registerDto.getEmail());
            if(u != null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is exists, please choose an other email");
            }
            registerDto.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
            service.createAccount(registerDto);
//            Authentication authentication = authenticationManager.authenticate(new
//                    UsernamePasswordAuthenticationToken(registerDto.getEmail(),registerDto.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(new JwtResponseDto(registerDto.getEmail(), "token"));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("register not success");
    }

    @Override
    public ResponseEntity<?> login(LoginDto login) {
        try{
            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(new JwtResponseDto(login.getEmail(), token));
        }catch (Exception ex){
            System.out.println((ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password not correct");
    }

    public UserEntity checkUser(String email){
        UserEntity u = null;
        try{
            u = service.findUserByEmail(email);
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

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
