package com.lev.ParsingPDF.service;

import com.lev.ParsingPDF.entity.Bill;
import com.lev.ParsingPDF.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParsingPDFServiceImpl implements ParsingPDFService {
    private final BillRepository billRepository;
    private Bill bill;
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            if(!Files.exists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Bill getBillById(long id) {
        return billRepository.findById(id).get();
    }

    @Override
    public List<Bill> getAllByType(String type) {
        return billRepository.findAllByTypeBill(type);
    }

    @Override
    public Double getAmountOfAllBills() {
        return billRepository.findAllAmount();
    }


    @Override
    public Bill fetchingInfoFromFile(MultipartFile file) throws IOException {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        byte[] bytes = file.getBytes();
        File serverFile = new File(root + File.separator +
                FilenameUtils.removeExtension(file.getOriginalFilename()) + ".txt");
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();
        PDFTextStripper s = new PDFTextStripper();
        String content = s.getText(PDDocument.load(serverFile));
        try ( PrintWriter out = new PrintWriter(new File(root + File.separator +
                FilenameUtils.removeExtension(file.getOriginalFilename()) + ".txt"))){
            out.println(content);
        }

        if (content.contains("החשמל")){
            parsingElecticBill(content);
        } else if (content.contains("המים")){
            parsingWaterBill(content);
        } else if (content.contains("פזגז")){
            parsingGasBill(content);
        } else if (content.contains("partner")){
            parsingInternetBill(content);
        } else {
            log.error("Wrong bill, impossible to parse");
        }
        billRepository.save(bill);
        return bill;
    }

    private Bill parsingInternetBill(String content) {
        long billNumber = 0l;
        String [] string = content.split("\n");
        billNumber = Long.parseLong(string[25].trim().split(" ")[0]);


        String tillDate = "";
        String fromDate = "";
        for(String ss:string ) {
            if (ss.contains("החשבון:תקופת")) {
                String[] sss = ss.trim().split("-");
                fromDate = sss[0];
                tillDate = sss[1].split("החשבון:תקופת")[0];
                break;
            }
        }

        Double amount = 0.0;
        String[] fixDouble = string[12].trim().split("\\{")[0].split("\\.");
        amount = Double.parseDouble(fixDouble[1] + "."+ fixDouble[0]);
        bill = Bill.builder().typeBill("InternetBill").billNumber(billNumber).fromDate(fromDate).tillDate(tillDate).amount(amount).build();
        return bill;

    }

    private Bill parsingGasBill(String content) {
        long billNumber = 0l;
        String [] string = content.split("\n");
        for(String ss:string ) {
            if (ss.contains("מס' חשבונית")) {
                long l = Long.parseLong(ss.replaceAll("\\D", ""));
                log.info("NumberBill: " + l);
                billNumber = l;
                break;
            }
        }
        String tillDate = "";
        String fromDate = "";
        for(String ss:string ) {
            if (ss.contains("תאריR הפקה")) {
                String[] sss = ss.trim().split(" ");
                fromDate = sss[2];
                break;
            }
        }

            for(String ss1:string ) {
                if (ss1.contains("תאריR משלוח")) {
                    String[] sss = ss1.trim().split(" ");
                    tillDate = sss[2];
                    break;
                }
            }

        Double amount = 0.0;
        for(String ss2:string ) {
            if (ss2.contains("סה''כ לתשלוU כולל מע''מ")) {
                String [] sss= ss2.trim().split(" ");
                double d = Double.parseDouble(sss[1]);
                amount = d;
                log.info("Amount: " + d);
                break;
            }
        }
        bill = Bill.builder().typeBill("GasBill").billNumber(billNumber).fromDate(fromDate).tillDate(tillDate).amount(amount).build();
        return bill;
    }

    private Bill parsingWaterBill(String content) {
        String[] string = content.split("\n");
        long billNumber = 0l;
        for (String ss : string) {
            if (ss.contains("הזוח ןובשח 'סמ")) {
                long l = Long.parseLong(ss.replaceAll("\\D", ""));
                log.info("NumberBill: " + l);
                billNumber = l;
                break;
            }
        }

            String[] s1 = string[28].trim().split(" ");
            String fromDate = s1[2];
            String tillDate = s1[0];


            //Amount to return
            Double amount = 0.0;
            String[] s2 = string[1].trim().split(" ");
            amount = Double.parseDouble(s2[1]);
            bill = Bill.builder().typeBill("WaterBill").billNumber(billNumber).fromDate(fromDate).tillDate(tillDate).amount(amount).build();
            return bill;
        }


    private Bill parsingElecticBill(String content) {

        long billNumber = 0l;
        String [] string = content.split("\n");
        for(String ss:string ) {
            if (ss.contains("מספר חשבון חוזה")) {
                long l = Long.parseLong(ss.replaceAll("\\D", ""));
                log.info("NumberBill: " + l);
                billNumber = l;
                break;
            }
        }
        String tillDate = "";
        String fromDate = "";
        for(String ss:string ) {
            if (ss.contains("מ- ")) {
                String[] sss = ss.trim().split(" ");
                tillDate = sss[4];
                fromDate = sss[1];
                break;
            }
        }
        Double amount = 0.0;
        for(String ss:string ) {
            if (ss.contains("חסה\"כ לתשלום (ש\"ח)")) {
                String [] sss= ss.trim().split(" ");
                double d = Double.parseDouble(sss[0]);
                amount = d;
                log.info("Amount: " + d);
                break;
            }
        }
        bill = Bill.builder().typeBill("ElectricBill").billNumber(billNumber).fromDate(fromDate).tillDate(tillDate).amount(amount).build();

        return bill;
    }
}