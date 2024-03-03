package com.example.beprojectsem4.dtos.subprogramDtos;

import java.util.Date;

import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;

public class SubProgramsDto {
	    private Long subProgramId;	  
	    private UserEntity user;
	    private String type;	 
	    private ProgramEntity program;
	    private Date createAt;	  
	    private Date updatedAt;
		public Long getSubProgramId() {
			return subProgramId;
		}
		public void setSubProgramId(Long subProgramId) {
			this.subProgramId = subProgramId;
		}
		public UserEntity getUser() {
			return user;
		}
		public void setUser(UserEntity user) {
			this.user = user;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public ProgramEntity getProgram() {
			return program;
		}
		public void setProgram(ProgramEntity program) {
			this.program = program;
		}
		public Date getCreateAt() {
			return createAt;
		}
		public void setCreateAt(Date createAt) {
			this.createAt = createAt;
		}
		public Date getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
		}
	    
}
