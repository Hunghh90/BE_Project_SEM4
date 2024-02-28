package com.example.beprojectsem4.dtos.programDtos;

import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import com.example.beprojectsem4.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProgramDto {
    public String programName;
    public Long target;
    public Date startDonateDate;
    public Date endDonateDate;
    public Date finishDate;
    public String description;
    public boolean finishSoon;
    public boolean recruitCollaborators;
    public Long partnerId;
    public List<String> imageUrl;
}
