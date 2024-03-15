package com.example.beprojectsem4.dto.request;

import com.example.beprojectsem4.entities.ProgramEntity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestDonate {
    private Long amount;
    private String description;
    @Pattern(regexp = "^(VNPay|Paypal)$", message = "Invalid payment method. Allowed values are 'VNPay' or 'Paypal'")
    private String paymentMethod;
    private Long programId;
    private Long userId;
}
