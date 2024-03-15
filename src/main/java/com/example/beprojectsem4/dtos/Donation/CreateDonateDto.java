package com.example.beprojectsem4.dtos.Donation;

import com.example.beprojectsem4.dtos.userDtos.GetMeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDonateDto {
    private Long id;
    private Long userId;
    private Double amount;
    @Nullable
    private String paymentMethod;
    @Nullable
    private String description;
}
