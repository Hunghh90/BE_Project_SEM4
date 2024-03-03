package com.example.beprojectsem4.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.beprojectsem4.dtos.subprogramDtos.CreateSubProgramDto;
import com.example.beprojectsem4.dtos.subprogramDtos.GetSubProgramsDto;
import com.example.beprojectsem4.dtos.subprogramDtos.SubProgramsDto;
import com.example.beprojectsem4.dtos.subprogramDtos.UpdateSubProgramsDto;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.SubProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.helper.TransferValuesIfNull;
import com.example.beprojectsem4.repository.SubProgramRepository;
import com.example.beprojectsem4.service.SubProgramService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class SubProgramServiceImpl implements SubProgramService {
 
	
	@Autowired
	private SubProgramRepository subprogramRepository;
	 @PersistenceContext
	    private EntityManager entityManager;
	@Override
	@Transactional
	public ResponseEntity<?> createSubprogram(CreateSubProgramDto createSubProgramDto) {
		 try {
		        SubProgramEntity subprogram = EntityDtoConverter.convertToEntity(createSubProgramDto, SubProgramEntity.class);
		        UserEntity user = entityManager.find(UserEntity.class, createSubProgramDto.getUserid());
		        ProgramEntity program = entityManager.find(ProgramEntity.class, createSubProgramDto.getProgramid());
		           
		           
		        if(user != null && program != null)   { 
		          
		            subprogram.setUser(user);
		            subprogram.setProgram(program);
		            entityManager.persist(subprogram);
		            subprogramRepository.save(subprogram);
		            return ResponseEntity.status(HttpStatus.CREATED).body("Create subprogram success");
		        } else {
		           
		            return ResponseEntity.badRequest().body("cannot Create");
		        }} catch (Exception ex){
    System.out.println(ex.getMessage());
    return ResponseEntity.internalServerError().body(ex.getMessage());
}

	}

	@Override
	public ResponseEntity<?> getSubprogram(Long id) {
		try{SubProgramEntity subprograms= subprogramRepository.findById(id).get();
		return ResponseEntity.ok().body(subprograms);
	}catch (Exception e){
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
	}

	@Override
	public ResponseEntity<?> listSubprogram(GetSubProgramsDto getSubProgramsDto) {
		  try {
	            Sort sort = Sort.by(Sort.Order.desc("createAt"));
	            PageRequest pageRequest = PageRequest.of(getSubProgramsDto.getPage(), getSubProgramsDto.getSize(),sort);
	            Page<SubProgramEntity> subprograms = subprogramRepository.findBytypeContaining(getSubProgramsDto.getType(),pageRequest);
	            List<SubProgramsDto> subprpDtos = new ArrayList<>();
	            for (SubProgramEntity sub : subprograms){
	                SubProgramsDto subproDto = EntityDtoConverter.convertToDto(sub,SubProgramsDto.class);
	                subprpDtos.add(subproDto);
	            }
	            return ResponseEntity.ok().body(subprpDtos);
	        }catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }
	}

	@Override
	public ResponseEntity<?> updateSubprogram(Long id, UpdateSubProgramsDto updateSubProgramsDto) {
		  try {
	            Optional<SubProgramEntity> subprogram= subprogramRepository.findById(id);
	           
	            if (subprogram.isPresent()) {
	            	SubProgramEntity sub = EntityDtoConverter.convertToEntity(updateSubProgramsDto,SubProgramEntity.class);
	            	System.out.println(sub);
	            	
	            	SubProgramEntity subn = TransferValuesIfNull.transferValuesIfNull(subprogram.get(),sub);
	            	System.out.println(subn);
	            	
	 		           
	            	subprogramRepository.save(subn);
	                return ResponseEntity.ok().body("Update success");
	            }
	      
	        }catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }
	        return null;
	}
}
