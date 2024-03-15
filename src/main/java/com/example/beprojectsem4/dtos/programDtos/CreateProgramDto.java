package com.example.beprojectsem4.dtos.programDtos;

import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import com.example.beprojectsem4.entities.UserEntity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class CreateProgramDto {
    private String programName;
    private Long target;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDonateDate;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDonateDate;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finishDate;
    private String description;
    private boolean finishSoon;
    private boolean recruitCollaborators;
    private Long partnerId;
    private List<String> imageUrl;
}
