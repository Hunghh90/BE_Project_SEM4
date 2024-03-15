package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.config.VNPayConfig;
import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dto.response.ResponseDonate;
import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.impl.PaypalService;
import com.example.beprojectsem4.service.impl.VNPayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/pay-return")
    public RedirectView orderReturn(HttpServletRequest request) {
        return donationService.payReturn(request);
    }

//    @PostMapping("/all-donate-by-program")
//    public ResponseEntity<?> listProgramByStatus(PaginateAndSearchByNameDto paginateAndSearchByNameDto){
//        return donationService.listDonateByProgramName(paginateAndSearchByNameDto);
//    }

}
