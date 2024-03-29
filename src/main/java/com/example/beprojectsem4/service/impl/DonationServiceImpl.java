package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.config.VNPayConfig;
import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dto.response.ResponseDonate;
import com.example.beprojectsem4.dtos.Donation.CreateDonateDto;
import com.example.beprojectsem4.dtos.Donation.DonateDto;
import com.example.beprojectsem4.dtos.common.PaginateAndSearchByNameDto;
import com.example.beprojectsem4.dtos.programDtos.ProgramDto;
import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.DonationRepository;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DonationServiceImpl implements DonationService {
    @Autowired
    private ProgramService programService;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    UserService userService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaypalService paypalService;
    @Override
    public ResponseEntity<?> donationSuccess(HttpServletRequest request, CreateDonateDto donateDto) {
        try{
            ProgramEntity programEntity = programService.addMoneyDonate(donateDto);
            if(programEntity != null){
                DonationEntity donate = EntityDtoConverter.convertToEntity(donateDto,DonationEntity.class);
                UserEntity user = userService.findUserByToken(request);
                if(user != null){
                    donate.setUser(user);
                    donate.setProgram(programEntity);
                }
                donationRepository.save(donate);
                return ResponseEntity.ok().body("Success");
            }else {
                return ResponseEntity.badRequest().body("Not success");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

//    @Override
//    public RedirectView payReturn(HttpServletRequest request) {
//        try{
//            Map fields = new HashMap();
//            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
//                String fieldName = null;
//                String fieldValue = null;
//                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
//                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
//                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
//                    fields.put(fieldName, fieldValue);
//                }
//            }
//            String programId = request.getParameter("ProgramId");
//            String userId = request.getParameter("UserId");
//            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//            fields.remove("vnp_SecureHashType");
//            fields.remove("vnp_SecureHash");
//            String signValue = VNPayConfig.hashAllFields(fields);
//            String vnpAmountParam = request.getParameter("vnp_Amount");
//
//            if (programId != null && !programId.isEmpty() && vnpAmountParam != null && userId !=null) {
//                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
//                    if("Paypal".equals(request.getParameter("payment_Method"))) {
//                        Double vnpAmount = Double.parseDouble(vnpAmountParam);
//                        CreateDonateDto donateDto = new CreateDonateDto();
//                        donateDto.setId(Long.valueOf(request.getParameter("ProgramId")));
//                        donateDto.setUserId(Long.valueOf(request.getParameter("UserId")));
//                        donateDto.setAmount(vnpAmount/100);
//                        donateDto.setPaymentMethod("Paypal");
//                        DonationSuccess(donateDto);
//                        return new RedirectView("localhost:3000/payment-success");
//                    }
//                    Double vnpAmount = Double.parseDouble(vnpAmountParam);
//                    CreateDonateDto donateDto = new CreateDonateDto();
//                    donateDto.setId(Long.valueOf(request.getParameter("ProgramId")));
//                    donateDto.setUserId(Long.valueOf(request.getParameter("UserId")));
//                    donateDto.setAmount(vnpAmount/100);
//                    donateDto.setPaymentMethod("VNPay");
//                    DonationSuccess(donateDto);
//                    return new RedirectView("localhost:3000/payment-success");
//                } else {
//                    return new RedirectView("localhost:3000/payment-success");
//                }
//            } else {
//                return new RedirectView("localhost:3000/payment-success");
//            }
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//            return new RedirectView("localhost:3000/payment-success");
//        }
//    }

//    @Override
//    public ResponseEntity<?> listDonateByProgramName(PaginateAndSearchByNameDto paginateAndSearchByNameDto) {
//        try {
//            if(paginateAndSearchByNameDto.getPage() <=0){
//                paginateAndSearchByNameDto.setPage(1);
//            }
//            if(paginateAndSearchByNameDto.getSize()<=0){
//                paginateAndSearchByNameDto.setSize(20);
//            }
//            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
//            PageRequest pageRequest = PageRequest.of(paginateAndSearchByNameDto.getPage()-1, paginateAndSearchByNameDto.getSize(),sort);
//            Page<DonationEntity> donationEntityPage = donationRepository.findByProgramProgramNameContaining(paginateAndSearchByNameDto.getName(),pageRequest);
//            if(donationEntityPage.isEmpty()){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found donations");
//            }
//            List<DonateDto> donateDtoList = new ArrayList<>();
//            for(DonationEntity donation : donationEntityPage){
//                DonateDto donateDto = EntityDtoConverter.convertToDto(donation, DonateDto.class);
//                donateDtoList.add(donateDto);
//            }
//            return ResponseEntity.ok().body(donateDtoList);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    @Override
    public ResponseEntity<byte[]> generateDonationPDF(Long programId) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        ResponseEntity<?> programDetail = programService.detailProgram(programId);
        if(programDetail.getStatusCode().is2xxSuccessful()){
            ProgramDto program = (ProgramDto) programDetail.getBody();
            assert program != null;
//            File pdfFile = new File(program.getProgramName() + ".pdf");
//            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            List<DonateDto> donations = program.getDonations();
            document.add(new Paragraph("List donations " + program.getProgramName()));
            for (DonateDto donation : donations) {
                String amountVND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(donation.getAmount());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String createdAtFormatted = dateFormat.format(donation.getCreatedAt());
                document.add(new Paragraph(donation.getUser().getDisplayName() + " - " + amountVND + " - " + donation.getPaymentMethod() + " - " + createdAtFormatted));
            }
            document.close();
            String fileName = program.getProgramName().replace(" ", "_");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName+".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Object> makePayment(RequestDonate paymentRequest) {
        try{
            String paymentMethod = paymentRequest.getPaymentMethod();
            if ("VNPay".equals(paymentMethod) ) {
                ResponseDonate responseDonate = new ResponseDonate();
                responseDonate.setMessageCode("200");
                responseDonate.setUrl(vnPayService.createOrder(paymentRequest));
                responseDonate.setMessage("Request successfully fulfilled");
                return ResponseEntity.status(HttpStatus.OK).body(responseDonate);

            } else if ("Paypal".equals(paymentMethod)) {
                ResponseDonate responseDonate = new ResponseDonate();
                responseDonate.setMessageCode("200");
                responseDonate.setMessage("Request successfully fulfilled");
                responseDonate.setUrl(paypalService.getLinksPayment(paymentRequest));
                return ResponseEntity.status(HttpStatus.OK).body(responseDonate);
            } else {
                ResponseDonate responseDonate = new ResponseDonate();
                responseDonate.setMessageCode("400");
                responseDonate.setMessage("Invalid Payment Method");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDonate);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }


    }
}
