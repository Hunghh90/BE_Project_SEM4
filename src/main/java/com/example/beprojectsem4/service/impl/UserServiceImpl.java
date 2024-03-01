package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.userDtos.ChangePasswordDto;
import com.example.beprojectsem4.dtos.authDtos.JwtResponseDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.authDtos.TokenResponseDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.dtos.userDtos.ResetPasswordDto;
import com.example.beprojectsem4.dtos.userDtos.UpdateUserDto;
import com.example.beprojectsem4.entities.RoleEntity;
import com.example.beprojectsem4.entities.UserAttachmentEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.RoleRepository;
import com.example.beprojectsem4.repository.UserAttachmentRepository;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.UserService;
import com.example.beprojectsem4.service.jwt.JwtAuthenticationFilter;
import com.example.beprojectsem4.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserAttachmentRepository userAttachmentRepository;

    @Override
    public ResponseEntity<?> createAccountUser(RegisterDto registerDto) {
        try {
            UserEntity u = checkUser(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.badRequest().body("Email is exists");
            }
            UserEntity us = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            us.setPassword(bCryptPasswordEncoder.encode(us.getPassword()));
            us.setStatus("DeActivate");
            UserEntity user = repository.save(us);
            UserAttachmentEntity avatar = new UserAttachmentEntity(user,"Avatar", registerDto.getAvatarUrl() );
            userAttachmentRepository.save(avatar);
            return ResponseEntity.status(201).body("Create success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createAccountAdmin(RegisterDto registerDto) {
        try {
            UserEntity u = checkUser(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is exists, please choose an other email");
            }
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            RoleEntity userRole = roleRepository.findRoleByRoleName("USER");
            if (userRole == null) {
                userRole = new RoleEntity();
                userRole.setRoleName("ADMIN");
                roleRepository.save(userRole);
            }
            user.getRoles().add(userRole);
            user.setStatus("Activate");
            repository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Create success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred");
        }
    }

    @Override
    public ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto) {
        try {
            String token = jwtAuthenticationFilter.getToken(request);
            UserEntity user = jwtService.getMeFromToken(token);
            boolean checkPassword = bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword());
            if (!checkPassword) {
                return ResponseEntity.badRequest().body("Old password not correct");
            }
            if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Confirm password not correct");

            }
            user.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword()));
            repository.save(user);
            return ResponseEntity.ok().body("Change password success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("Change password not success " + ex.getMessage());
        }

    }

    @Override
    public void saveRefreshToken(String email, String refreshToken) {
        UserEntity user = repository.findUserByEmail(email);
        user.setRefreshToken(refreshToken);
        repository.save(user);
    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            UserEntity user = findUserByToken(request);
            if (jwtService.validateRefreshToken(user.getRefreshToken())) {
                Authentication authentication = authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TokenResponseDto tokenResponseDto = jwtService.generateToken(authentication);
                return ResponseEntity.ok(new JwtResponseDto(user.getEmail(), tokenResponseDto.getToken(), tokenResponseDto.getExpired()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public UserEntity checkUser(String email) {

        try {
            return repository.findUserByEmail(email);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        try{
            UserEntity user = findUserByToken(request);
            GetMeDto gm = EntityDtoConverter.convertToDto(user, GetMeDto.class);
            return ResponseEntity.ok().body(gm);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public String updateUser(HttpServletRequest request, UpdateUserDto updateUserDto) {
        try{
            UserEntity user = findUserByToken(request);
            if(user == null){
                return "User not found";
            }else{
                user.setBod(updateUserDto.getBod());
                user.setPhoneNumber(updateUserDto.getPhoneNumber());
                repository.save(user);
                return "Update user successfully";
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public String blockUser(String email) {
        try{
            UserEntity user = repository.findUserByEmail(email);
            if(user == null){
                return "Email not exists";
            }else{
                user.setStatus("Block");
                repository.save(user);
                return "Block user success";
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return "An error occurred";
        }

    }

    @Override
    public String activeUser(String email) {
        try {
            UserEntity user = repository.findUserByEmail(email);
            if (user == null) {
                return "Email not exists";
            } else {
                RoleEntity userRole = roleRepository.findRoleByRoleName("USER");
                if (userRole == null) {
                    userRole = new RoleEntity();
                    userRole.setRoleName("USER");
                    roleRepository.save(userRole);
                }
                user.getRoles().add(userRole);
                user.setStatus("Activate");
                repository.save(user);
                return "Active success";
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }

    }

    @Override
    public UserEntity findUserByToken(HttpServletRequest request){
        try{
            String token = jwtAuthenticationFilter.getToken(request);
            return jwtService.getMeFromToken(token);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteAccount() {
        try {
            List<UserEntity> users = repository.findAllByStatus("DeActivate");
            Date currentDate = new Date();
            for (UserEntity user : users) {
                long diffInMillies = Math.abs(currentDate.getTime() - user.getCreateAt().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (diff > 7) {
                    UserAttachmentEntity attachment = userAttachmentRepository.findUserAttachmentEntityByUser_UserId(user.getUserId());
                    userAttachmentRepository.delete(attachment);
                    repository.delete(user);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public boolean resetPassword(String email, ResetPasswordDto resetPasswordDto) {
        try{
            UserEntity user = checkUser(email);
            if(user == null){
                return false;
            }else{
                if(resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())){
                    String hashPassword = bCryptPasswordEncoder.encode(resetPasswordDto.getNewPassword());
                    user.setPassword(hashPassword);
                    repository.save(user);
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
