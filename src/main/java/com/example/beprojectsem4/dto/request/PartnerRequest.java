package com.example.beprojectsem4.dto.request;

import lombok.Data;

@Data
public class PartnerRequest {
    private String partnerName;
    public Integer page;
    public Integer size;
}
