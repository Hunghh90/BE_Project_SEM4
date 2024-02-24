package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.partner.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partner.PartnerDto;
import com.example.beprojectsem4.dtos.partner.UpdatePartnerDto;
import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.service.ImageUploadService;
import com.example.beprojectsem4.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PartnerController {
    @Autowired
    private PartnerService partnerService;

    @PostMapping("/create-partner")
    public void createPartner(@Nullable @RequestParam("files") List<MultipartFile> files, CreatePartnerDto createPartnerDto){
        partnerService.createPartner(files,createPartnerDto);
    }

    @GetMapping("/get-all-partner")
    public List<PartnerDto> listPartner(){
        return partnerService.listPartner();
    }
@GetMapping("/get-partner-by-search")
    public List<PartnerEntity> searchAllField(@RequestParam("search") String search){
        return partnerService.searchAllField(search);
    }

    @PostMapping("/update-partner")
    public void updatepartner(@Nullable List<MultipartFile> files,@RequestParam("id")Long id, UpdatePartnerDto updatePartnerDto){
        partnerService.updatepartner(files,id,updatePartnerDto);
    }
}
