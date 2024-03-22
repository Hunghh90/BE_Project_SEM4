package com.example.beprojectsem4.dtos.programDtos;

import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProgramDto {
    private Long programId;
    private String programName;
    private Long target;
    private Date startDonateDate;
    private Date endDonateDate;
    private Date finishDate;
    private String description;
    private String status;
    private boolean finishSoon;
    private boolean recruitCollaborators;
    private List<ProgramAttachmentEntity> attachment;
    private Double totalMoney;
    private Double donateByPaypal;
    private Double donateByVNPay;
    private Date createdAt;
    private String reasonRejection;
}
