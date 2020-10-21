package com.lev.ParsingPDF.service;

import com.lev.ParsingPDF.entity.Bill;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ParsingPDFServiceImpl implements ParsingPDFService {
    Logger logger = LoggerFactory.getLogger(ParsingPDFServiceImpl.class);
    Bill bill;
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
        try( PrintWriter out = new PrintWriter(new File(root + File.separator +
                FilenameUtils.removeExtension(file.getOriginalFilename()) + ".txt"))){
            out.println(content);
        }
        Long billNumber = 0l;
        String [] string = content.split("\n");
        for(String ss:string ) {
            if (ss.contains("מספר חשבון חוזה")) {
                long l = Long.parseLong(ss.replaceAll("\\D", ""));
                logger.info("NumberBill: " + l);
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
                logger.info("Amount: " + d);
                break;
            }
        }
        bill = Bill.builder().billNumber(billNumber).fromDate(fromDate).tillDate(tillDate).amount(amount).build();
        return bill;
    }
}