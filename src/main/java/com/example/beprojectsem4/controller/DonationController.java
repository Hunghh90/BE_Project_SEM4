package com.example.beprojectsem4.controller;

import com.example.beprojectsem4.config.VNPayConfig;
import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dto.response.ResponseDonate;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.impl.PaypalService;
import com.example.beprojectsem4.service.impl.VNPayService;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private VNPayService vnPayService;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private DonationService donationService;


    @PostMapping("/donation")
    public ResponseEntity<Object> makePayment(@Valid @RequestBody RequestDonate paymentRequest) {
        String paymentMethod = paymentRequest.getPaymentMethod();
        // Xử lý logic thanh toán ở đây
        if ("VNPay".equals(paymentMethod) ) {
            // Sử dụng VNPayService để xử lý thanh toán VNPay
            ResponseDonate responseDonate = new ResponseDonate();
            responseDonate.setMessageCode("200");
            responseDonate.setUrl(vnPayService.createOrder(paymentRequest));

            return ResponseEntity.status(HttpStatus.OK).body(responseDonate);

        } else if ("Paypal".equals(paymentMethod)) {
            // Chuyển hướng đến PaypalService
            ResponseDonate responseDonate = new ResponseDonate();
            responseDonate.setMessageCode("200");
            responseDonate.setUrl(paypalService.getLinksPayment(paymentRequest));
            return ResponseEntity.status(HttpStatus.OK).body(responseDonate);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Payment Method");
        }
    }

    @GetMapping("/pay-return")
    public String orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        String programId = request.getParameter("ProgramId");
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = VNPayConfig.hashAllFields(fields);
        String vnpAmountParam = request.getParameter("vnp_Amount");

        if (programId != null && !programId.equals("") && vnpAmountParam != null) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                if("Paypal".equals(request.getParameter("payment_Method"))) {
                    int vnpAmount = Integer.parseInt(vnpAmountParam);
                    donationService.DonationSuccess(Long.valueOf(request.getParameter("ProgramId")), vnpAmount,"Paypal");
                    return "OK";
                }
                int vnpAmount = Integer.parseInt(vnpAmountParam);
                double result = vnpAmount / 100.0;
                donationService.DonationSuccess(Long.valueOf(request.getParameter("ProgramId")), result,"VNPay");
                return "OK";
            } else {
                return "FAIL";
            }
        } else {
            return "ERROR";
        }
    }


}
