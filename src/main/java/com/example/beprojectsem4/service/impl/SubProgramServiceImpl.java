package com.example.beprojectsem4.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cloudinary.json.JSONObject;
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
	
	@Override
	
	public ResponseEntity<?> createSubprogram(CreateSubProgramDto createSubProgramDto) {
		 try {
		        SubProgramEntity subprogram = EntityDtoConverter.convertToEntity(createSubProgramDto, SubProgramEntity.class);
		        if(subprogram != null) {
		    	    UserEntity user=new UserEntity();
		    	    ProgramEntity program=new ProgramEntity();
		    	    program.setProgramId(createSubProgramDto.getProgramid());
		    	    user.setUserId(createSubProgramDto.getUserid());
		    	   
		    	    if (subprogramRepository.existsByUserAndProgram(user, program)) {
		    	      
		    	        return ResponseEntity.badRequest().body("User has already registered for this program");
		    	    } else {
		    	       
		    	        subprogram.setUser(user);
		    	        subprogram.setProgram(program);
		    	        subprogramRepository.save(subprogram);
		    	        return ResponseEntity.status(HttpStatus.CREATED).body("Create subprogram success");
		    	    }}
		        else {
		        	   return ResponseEntity.badRequest().body("Can't create subprogram");
		        }       
		 }
		 catch (Exception ex){
    System.out.println(ex.getMessage());
    return ResponseEntity.internalServerError().body(ex.getMessage());
}

	}

	@Override
	public ResponseEntity<?> getSubprogram(Long id) {
		try {
	        Optional<SubProgramEntity> subprogramOptional = subprogramRepository.findById(id);
	        if (subprogramOptional.isPresent()) {
	           SubProgramsDto responsedto=new SubProgramsDto();
	           SubProgramEntity subprogram = subprogramOptional.get();
	            UserEntity user = subprogram.getUser();
	            ProgramEntity program = subprogram.getProgram();
	            responsedto.setSubProgramId(id);
	            responsedto.setUserid(user.getUserId());
	            responsedto.setProgramid(program.getProgramId());
	            responsedto.setType(subprogram.getType());
	            responsedto.setCreateAt(subprogram.getCreateAt());
	            responsedto.setUpdatedAt(subprogram.getUpdatedAt());
	           return ResponseEntity.ok().body(responsedto);
	        } else {
	            return ResponseEntity.badRequest().body("Subprogram not found");
	        }
	    } catch (Exception e) {
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
	            	SubProgramEntity subn = TransferValuesIfNull.transferValuesIfNull(subprogram.get(),sub);
	            	
	            	 UserEntity user=new UserEntity();
			    	    ProgramEntity program=new ProgramEntity();
			    	    
			    	    program.setProgramId(updateSubProgramsDto.getProgramid());
			    	    user.setUserId(updateSubProgramsDto.getUserid());
			    	    subn.setUser(user);
			    	    subn.setProgram(program);
	 		           
	            	subprogramRepository.save(subn);
	                return ResponseEntity.ok().body("Update success");
	            }
	            else {
	            	return ResponseEntity.badRequest().body("Can not update");
	            }
	      
	        }catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }
	       
	}

	@Override
	public ResponseEntity<?> deleteSubprogram(Long id) {
		  try{
			  Optional<SubProgramEntity> subProgramOptional = subprogramRepository.findById(id);
	        if (subProgramOptional.isPresent()) {
	            SubProgramEntity subProgram = subProgramOptional.get();
	            subprogramRepository.delete(subProgram);
	            return ResponseEntity.ok().body("Delete success");
	        } else {
	        	return ResponseEntity.badRequest().body("Can't delete cause subprogram not found");
	        }}
		  
	        catch (Exception ex){
	            System.out.println(ex.getMessage());
	            return ResponseEntity.internalServerError().body(ex.getMessage());
	        }
	       
	}
}
