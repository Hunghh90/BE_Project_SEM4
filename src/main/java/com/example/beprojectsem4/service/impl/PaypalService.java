package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.config.PaypalPaymentIntent;
import com.example.beprojectsem4.config.PaypalPaymentMethod;
import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.exception.NotFoundException;
import com.example.beprojectsem4.repository.ProgramRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;


    @Autowired
    ProgramRepository programRepository;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    public Payment createPayment(
            Double total,
            String currency,
            PaypalPaymentMethod method,
            PaypalPaymentIntent intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException{

        Amount amount = new Amount();
        amount.setCurrency(currency);
       total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public String getLinksPayment (RequestDonate requestDonate) {
        Optional<ProgramEntity> findProgramId =  programRepository.findById(requestDonate.getProgramId());

        if(!findProgramId.isPresent()) {
            throw new NotFoundException("Not found Program");
        }

        try {
            String successUrl = String.format("http://localhost:8080/api/v1/pay-return?vnp_Amount=%s&ProgramId=%s&vnp_TransactionStatus=%s&payment_Method=%s",
                    requestDonate.getAmount(), requestDonate.getProgramId(), "00","Paypal");

            Payment payment = createPayment(Double.valueOf(requestDonate.getAmount()), "USD", PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.order, requestDonate.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                    successUrl);


//            donationService.Donation(requestDonate.getProgramId(), requestDonate.getAmount());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }

            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        return "success";
    }


    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }


}
