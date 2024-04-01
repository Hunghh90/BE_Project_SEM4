package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.partnerDtos.CreatePartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.GetListPartnerDto;
import com.example.beprojectsem4.dtos.partnerDtos.UpdatePartnerDto;
import com.example.beprojectsem4.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partner")
public class PartnerController {
    @Autowired
    private PartnerService partnerService;

    @PostMapping("/create-partner")
    public ResponseEntity<?> createPartner(@RequestBody CreatePartnerDto createPartnerDto) {
        return partnerService.createPartner(createPartnerDto);
    }

    @GetMapping("/get-all-partner")
    public ResponseEntity<?> listPartner(@Nullable PaginateAndSearchByNameDto paginateAndSearchByNameDto){
        return partnerService.listPartner(paginateAndSearchByNameDto);
    }
@GetMapping("/get-partner-by-search")
    public ResponseEntity<?> searchAllField(@RequestParam("search") String search){
        return partnerService.searchAllField(search);
    }

    @PostMapping("/update-partner")
    public ResponseEntity<?> updatepartner(@RequestParam("id")Long id,@RequestBody UpdatePartnerDto updatePartnerDto){
        return partnerService.updatePartner(id,updatePartnerDto);
    }

    @GetMapping("/toggle-lock-partner/{id}")
    public ResponseEntity<?> blockPartner(@PathVariable Long id,String value){
        return partnerService.toggleLockPartner(id,value);
    }

    @GetMapping("/detail-partner")
    public ResponseEntity<?> getPartner(@RequestParam("id") Long id){
        return partnerService.getPartner(id);
    }
}
