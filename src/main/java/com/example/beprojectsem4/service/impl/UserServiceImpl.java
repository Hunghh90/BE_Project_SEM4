package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.userDtos.ChangePassword;
import com.example.beprojectsem4.dtos.authDtos.JwtResponseDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.authDtos.TokenResponseDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.dtos.userDtos.ResetPassword;
import com.example.beprojectsem4.dtos.userDtos.UpdateUserDto;
import com.example.beprojectsem4.entities.RoleEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.RoleRepository;
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
import java.util.List;
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

    @Override
    public boolean createAccountUser(RegisterDto registerDto) {
        try {
            UserEntity u = checkUser(registerDto.getEmail());
            if (u != null) {
                return false;
            }
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            user.setStatus("DeActivate");
            repository.save(user);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
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
    public ResponseEntity<?> changePassword(HttpServletRequest request, ChangePassword changePassword) {
        try {
            String token = jwtAuthenticationFilter.getToken(request);
            UserEntity user = jwtService.getMeFromToken(token);
            boolean checkPassword = bCryptPasswordEncoder.matches(changePassword.getOldPassword(), user.getPassword());
            if (!checkPassword) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password not correct");
            }
            if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body("Confirm password not correct");

            }
            user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
            repository.save(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password not success " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password success");
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
    public GetMeDto getMe(HttpServletRequest request) {
        try{
            UserEntity user = findUserByToken(request);
            return EntityDtoConverter.convertToDto(user, GetMeDto.class);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<?> updateUser(HttpServletRequest request,UpdateUserDto updateUserDto) {
        try{
            UserEntity user = findUserByToken(request);
            if(user == null){
                return ResponseEntity.badRequest().body("User not found");
            }else{
                user.setBod(updateUserDto.getBod());
                user.setPhoneNumber(updateUserDto.getPhoneNumber());
                repository.save(user);
                return ResponseEntity.ok().body("Update user successfully");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<?> blockUser(String email) {
        try{
            UserEntity user = repository.findUserByEmail(email);
            if(user == null){
                return ResponseEntity.badRequest().body("Email not exists");
            }else{
                user.setStatus("Block");
                repository.save(user);
                return ResponseEntity.ok().body("Block user success");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred");
        }

    }

    @Override
    public ResponseEntity<?> activeUser(String email) {
        try {
            UserEntity user = repository.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("Email not exists");
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
                return ResponseEntity.ok().body("Active success");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred");
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
                    repository.delete(user);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public boolean resetPassword(String email,ResetPassword resetPassword) {
        try{
            UserEntity user = checkUser(email);
            if(user == null){
                return false;
            }else{
                if(resetPassword.getNewPassword().equals(resetPassword.getConfirmPassword())){
                    String hashPassword = bCryptPasswordEncoder.encode(resetPassword.getNewPassword());
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
