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
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.SubProgramService;
import com.example.beprojectsem4.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class SubProgramServiceImpl implements SubProgramService {
 
	
	@Autowired
	private SubProgramRepository subprogramRepository;
	@Autowired
	private UserService userService;
	@Autowired 
	private ProgramService programService;
	@Override
	public ResponseEntity<?> createSubprogram(CreateSubProgramDto createSubProgramDto,HttpServletRequest request) {
		 try {
		        SubProgramEntity subprogram = EntityDtoConverter.convertToEntity(createSubProgramDto, SubProgramEntity.class);
		        if(subprogram != null) {
		    	    UserEntity user=userService.findUserByToken(request);
		    	    ProgramEntity program=programService.FindById(createSubProgramDto.getProgramId());		    	      	    	   
		    	    if (subprogramRepository.existsByUserAndProgramAndType(user, program,subprogram.getType())) {		    	      
		    	        return ResponseEntity.badRequest().body("User has already registered for this program");
		    	    } 
		    	    else {		    	       
		    	        subprogram.setUser(user);
		    	        subprogram.setProgram(program);
		    	        subprogramRepository.save(subprogram);
		    	        return ResponseEntity.status(HttpStatus.CREATED).body("Create subprogram success");
		    	    }
		    	    }
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
	       Optional<SubProgramEntity> subprogram = subprogramRepository.findById(id);
	        if (subprogram.isPresent()) {
	        	SubProgramEntity subEntity=subprogram.get();
	        	SubProgramsDto subdto=EntityDtoConverter.convertToDto(subEntity, SubProgramsDto.class);
	        	UserEntity user=subEntity.getUser();
	        	ProgramEntity program=subEntity.getProgram();
	        	subdto.setUserId(user.getUserId());
	        	subdto.setProgramId(program.getProgramId());
	           return ResponseEntity.ok().body(subdto);
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
	public ResponseEntity<?> updateSubprogram(Long id, UpdateSubProgramsDto updateSubProgramsDto,HttpServletRequest request) {
		  try {
	            Optional<SubProgramEntity> subprogram= subprogramRepository.findById(id);
	           
	            if (subprogram.isPresent()) {
	            	SubProgramEntity sub = EntityDtoConverter.convertToEntity(updateSubProgramsDto,SubProgramEntity.class);            	
	            	SubProgramEntity subn = TransferValuesIfNull.transferValuesIfNull(subprogram.get(),sub);            	
	                    UserEntity user=userService.findUserByToken(request);
	                    ProgramEntity program=programService.FindById(updateSubProgramsDto.getProgramId());		
			    	    program.setProgramId(updateSubProgramsDto.getProgramId());			    	
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
