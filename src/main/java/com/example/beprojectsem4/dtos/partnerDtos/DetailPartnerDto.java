package com.example.beprojectsem4.dtos.partnerDtos;

import com.example.beprojectsem4.dtos.programDtos.DetailProgramDto;
import com.example.beprojectsem4.entities.PartnerAttachmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailPartnerDto {
    private Long partnerId;
    private String partnerName;
    private String email;
    private String description;
    private String paypalAccount;
    private String vnpayAccount;
    private List<PartnerAttachmentEntity> attachment;
    private String status;
    private Date createdAt;
    private List<DetailProgramDto> programs;
}
