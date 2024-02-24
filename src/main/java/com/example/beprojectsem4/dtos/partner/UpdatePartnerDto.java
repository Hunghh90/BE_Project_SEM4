package com.example.beprojectsem4.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdatePartnerDto  {
    private String partnerName;
    private String email;
    private String description;
}
