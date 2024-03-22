package com.example.beprojectsem4.service;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface DonationService {
     ResponseEntity<?> donationSuccess(HttpServletRequest request, CreateDonateDto donateDto);
     ResponseEntity<Object> makePayment(RequestDonate paymentRequest);

//     RedirectView payReturn(HttpServletRequest request);
     ResponseEntity<byte[]> generateDonationPDF(Long programId) throws DocumentException, FileNotFoundException, IOException;
//     ResponseEntity<?> listDonateByProgramName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
//     ResponseEntity<?> listDonateByUserName(PaginateAndSearchByNameDto paginateAndSearchByNameDto);
}
