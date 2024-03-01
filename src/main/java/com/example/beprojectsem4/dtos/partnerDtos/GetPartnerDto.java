package com.example.beprojectsem4.dtos.partnerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPartnerDto {
    private String partnerName;
    private int page;
    private int size;
}
