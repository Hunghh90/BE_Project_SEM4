package com.example.beprojectsem4.dto.response;

import com.example.beprojectsem4.entities.PartnerEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartnerResponse {
    private List<PartnerEntity> list;
    private long total;

    public PartnerResponse(List<PartnerEntity> list,long total) {
        this.list = list;
        this.total = total;
    }
}
