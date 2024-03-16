package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.dtos.Donation.DonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.CreateProgramDto;
import com.example.beprojectsem4.dtos.programDtos.GetProgramsDto;
import com.example.beprojectsem4.dtos.programDtos.ProgramDto;
import com.example.beprojectsem4.dtos.programDtos.UpdateProgramDto;
import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import com.example.beprojectsem4.entities.*;
import com.example.beprojectsem4.exception.NotFoundException;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.DynamicSpecification;
import com.example.beprojectsem4.repository.ProgramAttachmentRepository;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.example.beprojectsem4.service.PartnerService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramAttachmentRepository programAttachmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PartnerService partnerService;
    @Override
    public ResponseEntity<?> createProgram(HttpServletRequest request, CreateProgramDto createProgramDto) {

        try{
            UserEntity user = userService.findUserByToken(request);
            UserEntity m = EntityDtoConverter.convertToEntity(user,UserEntity.class);
            PartnerEntity pn = (PartnerEntity) partnerService.getPartnerByEmail(user.getEmail()).getBody();
            if(!checkProgramByProgramName(createProgramDto.getProgramName()) && createProgramDto.getStartDonateDate().before(createProgramDto.getEndDonateDate()) && createProgramDto.getEndDonateDate().before(createProgramDto.getFinishDate())){
                ProgramEntity program = EntityDtoConverter.convertToEntity(createProgramDto,ProgramEntity.class);
                program.setStatus("DeActive");
                program.setUser(m);
                program.setPartner(pn);
                ProgramEntity pr = programRepository.save(program);
                for (String url : createProgramDto.getImageUrl()){
                    ProgramAttachmentEntity pa = new ProgramAttachmentEntity(pr,"Certify",url);
                    programAttachmentRepository.save(pa);
                }
                ProgramAttachmentEntity lg = new ProgramAttachmentEntity(pr,"Logo",createProgramDto.getImageLogo());
                programAttachmentRepository.save(lg);
                return ResponseEntity.ok().body("Create program success");
            }
            return ResponseEntity.badRequest().body("Program name is already or The date entered is incorrect");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> listProgram(PaginateAndSearchByNameDto paginateAndSearchByNameDto) {
        try{
            if(paginateAndSearchByNameDto.getPage() <=0 ){
                paginateAndSearchByNameDto.setPage(1);
            }
            if(paginateAndSearchByNameDto.getSize()<=0){
                paginateAndSearchByNameDto.setSize(20);
            }
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageable = PageRequest.of(paginateAndSearchByNameDto.getPage()-1, paginateAndSearchByNameDto.getSize(),sort);
            Page<ProgramEntity> programs = programRepository.findByProgramNameContaining(paginateAndSearchByNameDto.getName(),pageable );
            List<ProgramDto> programDtoList = new ArrayList<>();
            for (ProgramEntity p : programs){
                List<DonateDto> donateDtoList = new ArrayList<>();
                for(DonationEntity getDonate : p.getDonations()){
                    DonateDto donateDto = EntityDtoConverter.convertToDto(getDonate, DonateDto.class);
                    GetMeDto gm = EntityDtoConverter.convertToDto(getDonate.getUser(), GetMeDto.class);
                    donateDto.setUser(gm);
                    donateDtoList.add(donateDto);
                }
                Collections.sort(donateDtoList, Comparator.comparing(DonateDto::getCreatedAt).reversed());
                ProgramDto programDto = EntityDtoConverter.convertToDto(p,ProgramDto.class);
                programDto.setDonations(donateDtoList);
                programDtoList.add(programDto);
            }
            return ResponseEntity.ok().body(programDtoList);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateProgram(HttpServletRequest request, Long id, UpdateProgramDto updateProgramDto) {
        Date now = new Date();
        try {
            UserEntity user = userService.findUserByToken(request);
            UserEntity m = EntityDtoConverter.convertToEntity(user,UserEntity.class);
            Optional<ProgramEntity> pr = programRepository.findById(id);
            if(pr.isPresent() && pr.get().getStartDonateDate().before(now)){
                ProgramEntity updateProgram = EntityDtoConverter.convertToEntity(updateProgramDto,ProgramEntity.class);
                ProgramEntity up = TransferValuesIfNull.transferValuesIfNull(pr.get(),updateProgram);
                up.setUser(m);
                programRepository.save(up);
                if(updateProgramDto.getImageUrl() != null && !updateProgramDto.getImageUrl().isEmpty()){
                    List<ProgramAttachmentEntity> pa = programAttachmentRepository.findByProgramId_programId(pr.get().getProgramId());
                    programAttachmentRepository.deleteAll(pa);
                    for (String url : updateProgramDto.getImageUrl()){
                        ProgramAttachmentEntity img = new ProgramAttachmentEntity(pr.get(),"Certify",url);
                        programAttachmentRepository.save(img);
                    }
                }
                return ResponseEntity.ok().body("Update program success");
            }
            return ResponseEntity.badRequest().body("Program not exists or Update timed out");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> activekProgram(Long id) {
        try {
            Optional<ProgramEntity> programOptional = programRepository.findById(id);
            if (programOptional.isPresent() &&
                    (programOptional.get().getStatus().equals("DeActive") || programOptional.get().getStatus().equals("Block"))) {
                programOptional.get().setStatus("Active");
                programOptional.get().setTotalMoney(0.0);
                programRepository.save(programOptional.get());
                return ResponseEntity.ok().body("Active program success");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found program");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> blockProgram(Long id) {
        try {
            Optional<ProgramEntity> programOptional = programRepository.findById(id);
            if(programOptional.isPresent()){
                if(programOptional.get().getStartDonateDate().after(new Date())){
                    programOptional.get().setStatus("Block");
                    programRepository.save(programOptional.get());
                    return ResponseEntity.ok().body("Block program success");
                }else {
                    return ResponseEntity.badRequest().body("The program is already active and cannot be changed");
                }

            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found program");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public boolean checkProgramByProgramName(String programName) {
        try{
            ProgramEntity program = programRepository.findByProgramName(programName);
            if(program == null){
                return false;
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public ResponseEntity<?> searchAllField(String value) {
        try {
            Specification<ProgramEntity> spec = new DynamicSpecification<>(value);
            List<ProgramEntity> programs = programRepository.findAll(spec);
            if (programs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No program found for the given search criteria");
            }
            List<ProgramDto> programDtoList = new ArrayList<>();
            for(ProgramEntity p : programs){
                ProgramDto programDto = EntityDtoConverter.convertToDto(p,ProgramDto.class);
                programDtoList.add(programDto);
            }
            return ResponseEntity.ok().body(programDtoList);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> listProgramByStatus(PaginateAndSearchByNameDto paginateAndSearchByNameDto) {
        try{
                if(paginateAndSearchByNameDto.getPage() <=0){
                    paginateAndSearchByNameDto.setPage(1);
                }
                if(paginateAndSearchByNameDto.getSize()<=0){
                    paginateAndSearchByNameDto.setSize(20);
                }
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            PageRequest pageable = PageRequest.of(paginateAndSearchByNameDto.getPage()-1, paginateAndSearchByNameDto.getSize(),sort);
            Page<ProgramEntity> programs = programRepository.findByStatus(paginateAndSearchByNameDto.getName(),pageable );
            List<ProgramDto> programDtoList = new ArrayList<>();
            for (ProgramEntity p : programs){
                ProgramDto pd = EntityDtoConverter.convertToDto(p,ProgramDto.class);
                programDtoList.add(pd);
            }
            return ResponseEntity.ok().body(programDtoList);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> detailProgram(Long id) {
        try{
            Optional<ProgramEntity> program = programRepository.findById(id);
            if(program.isPresent()){
                List<DonateDto> donateDtoList = new ArrayList<>();
                for(DonationEntity getDonate : program.get().getDonations()){
                    DonateDto donateDto = EntityDtoConverter.convertToDto(getDonate, DonateDto.class);
                    GetMeDto gm = EntityDtoConverter.convertToDto(getDonate.getUser(), GetMeDto.class);
                    donateDto.setUser(gm);
                    donateDtoList.add(donateDto);
                }
                Collections.sort(donateDtoList, Comparator.comparing(DonateDto::getCreatedAt).reversed());
                ProgramDto programDto = EntityDtoConverter.convertToDto(program.get(),ProgramDto.class);
                programDto.setDonations(donateDtoList);
                return ResponseEntity.ok().body(programDto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found program");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ProgramEntity addMoneyDonate(CreateDonateDto donateDto) {
        try{
            Optional<ProgramEntity> programEntityOptional =  programRepository.findById(donateDto.getId());
            if(programEntityOptional.isEmpty()){
                throw new NotFoundException("Not found Program");
            }
            if(programEntityOptional.isPresent() && programEntityOptional.get().getStatus().equals("Coming soon")){
                ProgramEntity programEntity = programEntityOptional.get();
                programEntity.setTotalMoney(programEntity.getTotalMoney() + donateDto.getAmount());
                if(programEntity.isFinishSoon()){
                    int compareResult = Double.compare(programEntity.getTarget().doubleValue(), programEntity.getTotalMoney());
                    if(compareResult <= 0){
                        programEntity.setStatus("End");
                        programEntity.setEndDonateDate(new Date());
                    }
                }
                return programRepository.save(programEntity);
            }
            return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void changeStatusProgram(){
        try{
            List<ProgramEntity> programEntityList = programRepository.findAll();
            for(ProgramEntity program : programEntityList){
                if(program.getStartDonateDate().before(new Date()) && program.getStatus().equals("Active")){
                    program.setStatus("Coming soon");
                } else if (program.getEndDonateDate().before(new Date()) && program.getStatus().equals("Coming soon")) {
                    program.setStatus("Progress");
                } else if (program.getStatus().equals("Progress") && program.getEndDonateDate().after(new Date()) ) {
                    program.setStatus("End");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
