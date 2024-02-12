package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.authDtos.*;
import com.example.beprojectsem4.dtos.userDtos.ResetPassword;
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
import java.util.Objects;

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
            sendEmailService.sendEmail(registerDto.getEmail(),
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
        try {
            UserEntity user = userService.checkUser(login.getEmail());
            if (user != null && user.getStatus().equals("Activate")) {
                Authentication authentication = authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TokenResponseDto tokenResponseDto = jwtService.generateToken(authentication);
                String refreshToken = jwtService.generateRefreshToken(authentication);
                userService.saveRefreshToken(login.getEmail(), refreshToken);
                return ResponseEntity.ok(new JwtResponseDto(login.getEmail(), tokenResponseDto.getToken(), tokenResponseDto.getExpired()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not Activate or Block");

            }
        } catch (Exception ex) {
            System.out.println((ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password not correct");
    }


    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getToken(request);
        String email = jwtService.getUsernameFromToken(token);
        userService.saveRefreshToken(email, "");
        SecurityContextHolder.clearContext();
    }

    @Override
    public ResponseEntity<?> activeAccount(String code, String email) {
        try {
            boolean isMatch = bCryptPasswordEncoder.matches(email, code);
            if (isMatch) {
                if (Objects.equals(userService.activeUser(email), ResponseEntity.ok())) {
                    return ResponseEntity.ok("Success");
                }else {
                    return ResponseEntity.badRequest().body("Not Success");
                }
            } else {
                return ResponseEntity.badRequest().body("Not Success");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        try {
            UserEntity user = userService.checkUser(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("Email not exists");
            } else {
                sendEmailService.sendEmail(email, "Reset password", "http://www.localhost:8081/auth/reset-password?email="+email);
                return ResponseEntity.ok().body("Check email to reset password");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("An error");
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(String email,ResetPassword resetPassword) {
        try {
           if(userService.resetPassword(email,resetPassword)){
               return ResponseEntity.ok("Change password success");
           }else {
               return ResponseEntity.badRequest().body("Check password and confirm password");
           }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error");
        }
    }

}
