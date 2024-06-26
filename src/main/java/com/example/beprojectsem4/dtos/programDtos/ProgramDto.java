package com.example.beprojectsem4.dtos.programDtos;

import com.example.beprojectsem4.dtos.donationDtos.DonateDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDto {
    private Long programId;
    private String programName;
    private Long target;
    private Date startDonateDate;
    private Date endDonateDate;
    private Date finishDate;
    private String description;
    private String status;
    private boolean recruitCollaborators;
    private List<ProgramAttachmentEntity> attachment;
    private Double totalMoney;
    private PartnerEntity partner;
    private List<DonateDto> donations;
    private String reasonRejection;
    private int share;
    private int countVolunteer;
    private boolean isVolunteer;
    private boolean isSubscribe;
    private int countDonation;
}
