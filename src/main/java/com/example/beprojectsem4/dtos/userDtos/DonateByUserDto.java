package com.example.beprojectsem4.dtos.userDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonateByUserDto {
    private Long donationId;
    private Double amount;
    @Nullable
    private String paymentMethod;
    @Nullable
    private String description;
    private Date createdAt;
    private String programName;
}
