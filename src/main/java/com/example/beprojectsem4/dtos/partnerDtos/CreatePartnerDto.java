package com.example.beprojectsem4.dtos.partnerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartnerDto {
    private String partnerName;
    private String email;
    private String description;
    private String urlLogo;

}
