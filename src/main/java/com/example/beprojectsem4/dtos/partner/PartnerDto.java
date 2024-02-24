package com.example.beprojectsem4.dtos.partner;

import com.example.beprojectsem4.entities.PartnerAttachmentEntity;
import com.example.beprojectsem4.entities.PartnerEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
