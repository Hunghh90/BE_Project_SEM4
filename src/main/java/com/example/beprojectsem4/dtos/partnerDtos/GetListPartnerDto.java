package com.example.beprojectsem4.dtos.partnerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListPartnerDto {
    @Nullable
    private String partnerName;
    @Nullable
    private int page;
    @Nullable
    private int size;
}
