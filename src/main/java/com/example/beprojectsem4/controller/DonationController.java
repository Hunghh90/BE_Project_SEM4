package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dtos.DonationDtos.CreateDonateDto;
import com.example.beprojectsem4.service.DonationService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Tutorial", description = "Tutorial management APIs")
@RestController
@Validated
@RequestMapping("/api/v1")
public class DonationController {

    @Autowired
    private DonationService donationService;


    @PostMapping("/donation")
    public ResponseEntity<Object> makePayment(@Valid @RequestBody RequestDonate paymentRequest) {
        return donationService.makePayment(paymentRequest);
    }

    @PostMapping("/pay-return")
    public ResponseEntity<?> orderReturn(HttpServletRequest request,@RequestBody CreateDonateDto donateDto) {
        return donationService.donationSuccess(request,donateDto);
    }

//    @PostMapping("/all-donate-by-program")
//    public ResponseEntity<?> listProgramByStatus(PaginateAndSearchByNameDto paginateAndSearchByNameDto){
//        return donationService.listDonateByProgramName(paginateAndSearchByNameDto);
//    }

    @GetMapping("/download-donations")
    public ResponseEntity<byte[]> generateDonationPDF(Long programId) throws DocumentException, IOException {
        return donationService.generateDonationPDF(programId);
    }
}
