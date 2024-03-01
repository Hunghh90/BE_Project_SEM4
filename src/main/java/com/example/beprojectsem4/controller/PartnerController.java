package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.GetPartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.PartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.service.ImageUploadService;
import com.example.beprojectsem4.service.PartnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/partner")
public class PartnerController {
    @Autowired
    private PartnerService partnerService;

    @PostMapping("/create-partner")
    public void createPartner(@RequestBody CreatePartnerDto createPartnerDto) {
        partnerService.createPartner(createPartnerDto);
    }

    @GetMapping("/get-all-partner")
    public List<PartnerDto> listPartner(GetPartnerDto getPartnerDto){
        return partnerService.listPartner(getPartnerDto);
    }
@GetMapping("/get-partner-by-search")
    public List<PartnerEntity> searchAllField(@RequestParam("search") String search){
        return partnerService.searchAllField(search);
    }

    @PostMapping("/update-partner")
    public void updatepartner(@RequestParam("id")Long id, UpdatePartnerDto updatePartnerDto){
        partnerService.updatePartner(id,updatePartnerDto);
    }

    @GetMapping("/block-partner")
    public void blockPartner(@RequestParam("id") Long id){
        partnerService.blockPartner(id);
    }
}
