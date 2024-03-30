package com.example.beprojectsem4.service.impl;

import com.example.beprojectsem4.dto.request.RequestDonate;
import com.example.beprojectsem4.dto.response.ResponseDonate;
import com.example.beprojectsem4.dtos.donationDtos.CreateDonateDto;
import com.example.beprojectsem4.dtos.donationDtos.DonateDto;
import com.example.beprojectsem4.dtos.programDtos.ProgramDto;
import com.example.beprojectsem4.entities.DonationEntity;
import com.example.beprojectsem4.entities.ProgramEntity;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.helper.EntityDtoConverter;
import com.example.beprojectsem4.repository.DonationRepository;
import com.example.beprojectsem4.service.DonationService;
import com.example.beprojectsem4.service.ProgramService;
import com.example.beprojectsem4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    public ResponseEntity<byte[]> generateDonationPDF(HttpServletRequest request,Long programId) throws  IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream("src/main/resources/templates/ListDonate.docx"));
        ResponseEntity<?> programDetail = programService.detailProgram(request,programId);
        if(programDetail.getStatusCode().is2xxSuccessful()){
            ProgramDto program = (ProgramDto) programDetail.getBody();
            assert program != null;
            List<DonateDto> donations = program.getDonations();
            Double totalMoney = 0.0;
            int totalDonate = 0;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
            XWPFTable table = doc.getTables().get(0);
            for (DonateDto donation : donations) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(donation.getUser().getDisplayName());
                row.getCell(1).setText(decimalFormat.format(donation.getAmount()));
                row.getCell(2).setText(donation.getPaymentMethod());
                row.getCell(3).setText(dateFormat.format(donation.getCreatedAt()));
                totalMoney +=donation.getAmount();
                totalDonate +=1;
            }
            for (XWPFParagraph p : doc.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    String text = r.getText(0);
                    if (text != null && text.contains("program_name")) {
                        text = text.replace("program_name", program.getProgramName());
                        r.setText(text, 0);
                    } else if (text != null && text.contains("totalmoney")) {
                        text = text.replace("totalmoney", decimalFormat.format(totalMoney)+"VND");
                        r.setText(text, 0);
                    } else if (text != null && text.contains("totaldonate")) {
                        text = text.replace("totaldonate", String.valueOf(totalDonate));
                        r.setText(text, 0);
                    }
                }
            }
            FileOutputStream out = new FileOutputStream("ListDonate_modified.docx");
            doc.write(out);
            out.close();
            File pdfFile = new File("ListDonate_modified.pdf");
            FileInputStream docxInputStream = new FileInputStream("ListDonate_modified.docx");
            XWPFDocument documents = new XWPFDocument(docxInputStream);
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(documents, new FileOutputStream(pdfFile), options);
            byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", program.getProgramName()+".pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
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
