package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.authDtos.*;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.service.AuthService;
import com.example.beprojectsem4.service.SendEmailService;
import com.example.beprojectsem4.service.UserService;
import com.example.beprojectsem4.service.jwt.JwtAuthenticationFilter;
import com.example.beprojectsem4.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SendEmailService sendEmailService;
    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        try {
            if (!userService.createAccountUser(registerDto)) {
                return ResponseEntity.badRequest().body("Email is exists or an error occurred");
            }
            String hashedEmail = bCryptPasswordEncoder.encode(registerDto.getEmail());
            sendEmailService.sendActivationEmail(registerDto.getEmail(),
                    "Link Activate account",
                    "http://www.localhost:8081/auth/active?code=" + hashedEmail + "&email=" + URLEncoder.encode(registerDto.getEmail(), StandardCharsets.UTF_8));
            return ResponseEntity.ok().body("Check Email to activate account");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("register not success");
        }
    }

    @Override
    public ResponseEntity<?> login(LoginDto login) {
        try{
            UserEntity user = userService.checkUser(login.getEmail());
            if(user != null && user.getStatus().equals("Activate")){
                Authentication authentication = authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TokenResponseDto tokenResponseDto = jwtService.generateToken(authentication);
                String refreshToken = jwtService.generateRefreshToken(authentication);
                userService.saveRefreshToken(login.getEmail(),refreshToken);
                return ResponseEntity.ok(new JwtResponseDto(login.getEmail(),tokenResponseDto.getToken(),tokenResponseDto.getExpired()));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not Activate or Block");

            }
        }catch (Exception ex){
            System.out.println((ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password not correct");
    }



    public void logout(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getToken(request);
        String email = jwtService.getUsernameFromToken(token);
        userService.saveRefreshToken(email,"");
        SecurityContextHolder.clearContext();
    }

    @Override
    public ResponseEntity<?> activeAccount(String code,String email) {
        try{
            boolean isMatch = bCryptPasswordEncoder.matches(email,code);
            if(isMatch){
                UserEntity user = userService.checkUser(email);
                user.setStatus("Activate");

                return ResponseEntity.ok("Success");
            }else {
                return ResponseEntity.badRequest().body("Not Success");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
