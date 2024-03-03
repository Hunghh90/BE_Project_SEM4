package com.example.beprojectsem4.dtos.subprogramDtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubProgramDto {
	   
	    private Long userid;
	    private String type;
	    private Long programid;  
	    private Date createAt;
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
		public Date getCreateAt() {
			return createAt;
		}
		public void setCreateAt(Date createAt) {
			this.createAt = createAt;
		}  
	  
}
