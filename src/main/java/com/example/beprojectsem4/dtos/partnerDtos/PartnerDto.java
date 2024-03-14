package com.example.beprojectsem4.dtos.partnerDtos;

import com.example.beprojectsem4.dtos.programDtos.DetailProgramDto;
import com.example.beprojectsem4.entities.PartnerAttachmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDto {
    private Long partnerId;
    private String partnerName;
    private String email;
    private String description;
    private List<PartnerAttachmentEntity> attachment;
    private String status;
    private Date createdAt;
    private List<DetailProgramDto> programs;
}
