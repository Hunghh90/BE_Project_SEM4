package com.example.beprojectsem4.dtos.userDtos;

import com.example.beprojectsem4.dtos.subProgramDtos.SubProgramDto;
import com.example.beprojectsem4.entities.UserAttachmentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMeDto {
    private Long userId;
    private String email;
    private String displayName;
//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyyy/MM/dd")
//    private Date bod;
    private String phoneNumber;
    private UserAttachmentEntity avatarUrl;
    private String status;
    private String role;
    private Date updatedAt;
    @Nullable
    private List<DonateByUserDto> donations;
    @Nullable
    private List<SubProgramDto> subPrograms;
    @Nullable
    private Long partnerId;
}
