package com.example.beprojectsem4.dtos.subprogramDtos;

import java.util.Date;

import org.springframework.lang.Nullable;

public class UpdateSubProgramsDto {
	 @Nullable
	 private Long userid;
	 @Nullable
	 private String type;
	 @Nullable
	 private Long programid;  	 
	 @Nullable
	 private Date updatedAt;
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getProgramid() {
		return programid;
	}
	public void setProgramid(Long programid) {
		this.programid = programid;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	 
	 
}
