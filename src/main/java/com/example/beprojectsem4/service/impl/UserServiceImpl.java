package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.ProgramDto;
import com.example.beprojectsem4.dtos.subProgramDtos.SubProgramDto;
import com.example.beprojectsem4.dtos.userDtos.*;
import com.example.beprojectsem4.dtos.authDtos.JwtResponseDto;
import com.example.beprojectsem4.dtos.authDtos.RegisterDto;
import com.example.beprojectsem4.dtos.authDtos.TokenResponseDto;
import com.example.beprojectsem4.entities.*;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.PartnerRepository;
import com.example.beprojectsem4.repository.UserAttachmentRepository;
import com.example.beprojectsem4.repository.UserRepository;
import com.example.beprojectsem4.service.RoleService;
import com.example.beprojectsem4.service.UserService;
import com.example.beprojectsem4.service.jwt.JwtAuthenticationFilter;
import com.example.beprojectsem4.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserAttachmentRepository userAttachmentRepository;
    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public ResponseEntity<?> createAccountUser(RegisterDto registerDto) {
        try {
            UserEntity u = checkUser(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.badRequest().body("Email is exists");
            }
            if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
                return ResponseEntity.badRequest().body("Confirm password not correct");
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
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> createAccountAdmin(RegisterDto registerDto) {
        try {
            UserEntity u = checkUser(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is exists, please choose an other email");
            }
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
            RoleEntity userRole = roleService.findRoleByRoleName("ADMIN");
            if (userRole == null) {
                roleService.createRole("ADMIN");
                userRole = roleService.findRoleByRoleName("ADMIN");
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
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> createAccountPartner(RegisterDto registerDto) {
        try {
            UserEntity u = checkUser(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is exists, please choose an other email");
            }
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
            RoleEntity userRole = roleService.findRoleByRoleName("PARTNER");
            if (userRole == null) {
               roleService.createRole("PARTNER");
                userRole = roleService.findRoleByRoleName("PARTNER");
            }
            user.getRoles().add(userRole);
            user.setStatus("Activate");
            repository.save(user);
            UserAttachmentEntity avatar = new UserAttachmentEntity(user,"Avatar", registerDto.getAvatarUrl() );
            userAttachmentRepository.save(avatar);
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
            user.setUpdatedAt(new Date());
            repository.save(user);
            return ResponseEntity.ok().body("Change password success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("Change password not success " + ex.getMessage());
        }

    }

    @Override
    public void saveRefreshToken(String email, String refreshToken) {
        try{
            UserEntity user = repository.findUserByEmail(email);
            user.setRefreshToken(refreshToken);
            repository.save(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        try{
            UserEntity user = findUserByToken(request);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user");
            }
            String userRole = user.getRoles().iterator().next().getRoleName();
            List<DonateByUserDto> donations = new ArrayList<>();
            for(DonationEntity donation : user.getDonations()){
                DonateByUserDto donate = EntityDtoConverter.convertToDto(donation, DonateByUserDto.class);
                donate.setProgramName(donation.getProgram().getProgramName());
                donate.setProgramId(donation.getProgram().getProgramId());
                donations.add(donate);
            }
            List<SubProgramDto> subPrograms = new ArrayList<>();
            for(SubProgramEntity subProgram : user.getSubPrograms()){
                SubProgramDto subProgramDto = EntityDtoConverter.convertToDto(subProgram, SubProgramDto.class);
                subPrograms.add(subProgramDto);
            }
            GetMeDto gm = EntityDtoConverter.convertToDto(user, GetMeDto.class);
            if(userRole.equals("PARTNER")){
                PartnerEntity partner = partnerRepository.findByEmail(user.getEmail());
                assert partner != null;
                gm.setPartnerId(partner.getPartnerId());
            }
            gm.setRole(userRole);
            gm.setDonations(donations);
            gm.setSubPrograms(subPrograms);
            return ResponseEntity.ok().body(gm);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public String updateUser(HttpServletRequest request, UpdateUserDto updateUserDto) {
        try{
            UserEntity user = findUserByToken(request);
            if(user == null){
                return "User not found";
            }else{
                user.setPhoneNumber(updateUserDto.getPhoneNumber());
                repository.save(user);
                return "Update user successfully";
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> toggleLockUser(String email,String value) {
        try{
            UserEntity user = repository.findUserByEmail(email);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not exists");
            }else{
                user.setStatus(value);
                repository.save(user);
                return ResponseEntity.ok().body(value+" user success");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    public boolean activeUser(String email) {
        try {
            UserEntity user = repository.findUserByEmail(email);
            if (user == null) {
                return false;
            } else {
                RoleEntity userRole = roleService.findRoleByRoleName("USER");
                if (userRole == null) {
                    roleService.createRole("USER");
                    userRole = roleService.findRoleByRoleName("USER");
                }
                user.getRoles().add(userRole);
                user.setStatus("Activate");
                repository.save(user);
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
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
                long diffInMillies = Math.abs(currentDate.getTime() - user.getCreatedAt().getTime());
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
    public String resetPassword(String email) {
        try{
            UserEntity user = repository.findUserByEmail(email);
            if(user == null){
                return "Not found user";
            }
            String password = RandomStringUtils.random(8,true,true);
            String hashPassword = bCryptPasswordEncoder.encode(password);
            user.setPassword(hashPassword);
            repository.save(user);
            return password;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public UserEntity findUserById(Long id) {
        try{
            Optional<UserEntity> user = repository.findById(id);
            return user.orElse(null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        try{
            UserEntity user = findUserById(id);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
            }
            String userRole = user.getRoles().iterator().next().getRoleName();
            List<DonateByUserDto> donations = new ArrayList<>();
            for(DonationEntity donation : user.getDonations()){
                DonateByUserDto donate = EntityDtoConverter.convertToDto(donation, DonateByUserDto.class);
                donate.setProgramName(donation.getProgram().getProgramName());
                donate.setProgramId(donation.getProgram().getProgramId());
                donations.add(donate);
            }
            List<SubProgramDto> subPrograms = new ArrayList<>();
            for(SubProgramEntity subProgram : user.getSubPrograms()){
                SubProgramDto subProgramDto = EntityDtoConverter.convertToDto(subProgram, SubProgramDto.class);
                subProgramDto.setProgramId(subProgram.getProgram().getProgramId());
                subProgramDto.setProgramName(subProgram.getProgram().getProgramName());
                subPrograms.add(subProgramDto);
            }
            GetMeDto gm = EntityDtoConverter.convertToDto(user, GetMeDto.class);
            if(userRole.equals("PARTNER")){
                PartnerEntity partner = partnerRepository.findByEmail(user.getEmail());
                assert partner != null;
                gm.setPartnerId(partner.getPartnerId());
            }
            gm.setRole(userRole);
            gm.setDonations(donations);
            gm.setSubPrograms(subPrograms);
            return ResponseEntity.ok().body(gm);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> getAllUser(PaginateAndSearchByNameDto paginateAndSearchByNameDto) {
        try {
            PaginateAndSearchByNameDto paginate = new PaginateAndSearchByNameDto(paginateAndSearchByNameDto.getPage(), paginateAndSearchByNameDto.getSize());
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageRequest = PageRequest.of(paginate.getPage()-1,paginate.getSize(),sort);
            Page<UserEntity> users = repository.findAll(pageRequest);
            if(users.isEmpty()){
                return ResponseEntity.ok().body(new ArrayList<>());
            }
            List<GetMeDto> getMeDtos = new ArrayList<>();
            for(UserEntity user : users){
                String userRole = "USER";

                if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                    userRole = user.getRoles().iterator().next().getRoleName();
                }
                GetMeDto gm = EntityDtoConverter.convertToDto(user, GetMeDto.class);
                gm.setSubPrograms(new ArrayList<>());
                gm.setRole(userRole);
                getMeDtos.add(gm);
            }
            return ResponseEntity.ok().body(getMeDtos);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
