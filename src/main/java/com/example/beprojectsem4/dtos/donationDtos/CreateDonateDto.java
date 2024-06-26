package com.example.beprojectsem4.dtos.donationDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDonateDto {
    private Long programId;
    private Double amount;
    @Nullable
    private String paymentMethod;

}
