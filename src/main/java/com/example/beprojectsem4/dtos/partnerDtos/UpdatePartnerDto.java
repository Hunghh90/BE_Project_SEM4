package com.example.beprojectsem4.dtos.partnerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdatePartnerDto  {
    @Nullable
    private String partnerName;
    @Nullable
    private String email;
    @Nullable
    private String description;
}
