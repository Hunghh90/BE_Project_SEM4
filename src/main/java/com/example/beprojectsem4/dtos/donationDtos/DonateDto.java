package com.example.beprojectsem4.dtos.donationDtos;

import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonateDto {
    private Long donationId;
    private Double amount;
    @Nullable
    private String paymentMethod;
    @Nullable
    private String description;
    private GetMeDto user;
    private Date createdAt;
}
