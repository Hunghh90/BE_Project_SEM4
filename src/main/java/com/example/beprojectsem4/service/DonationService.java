package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

public interface DonationService {
     ResponseEntity<?> DonationSuccess( CreateDonateDto donateDto);
     ResponseEntity<Object> makePayment(RequestDonate paymentRequest);

     RedirectView payReturn(HttpServletRequest request);
//     ResponseEntity<?> listDonateByProgramName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
//     ResponseEntity<?> listDonateByUserName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
}
