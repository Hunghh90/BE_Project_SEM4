package com.example.beprojectsem4.dtos.Donation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonateDto {
    private Long id;
    private Double amount;
    @Nullable
    private String paymentMethod;
    @Nullable
    private String description;
}
