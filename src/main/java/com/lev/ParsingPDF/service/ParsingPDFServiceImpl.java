package com.lev.ParsingPDF.service;

import com.lev.ParsingPDF.entity.Bill;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ParsingPDFServiceImpl implements ParsingPDFService {
//Why doesn't work Bill bill;(NullPointerException)
    Bill bill= new Bill();


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
    public Bill parsingFile(MultipartFile file) throws IOException {

        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

//        try {
//            Files.copy(file.getInputStream(), this.root.resolve(file.getName() + ".txt"));
//        } catch (Exception e) {
//            throw new RuntimeException("Could not create txt file. Error: " + e.getMessage());
//        }




        byte[] bytes = file.getBytes();
//        File dir = new File("/home/lev/Desktop/Projects/ParsingPDF"
//                + File.separator + file.getName());
//        dir.mkdir();
        File serverFile = new File(root + File.separator + FilenameUtils.removeExtension(file.getOriginalFilename()) + ".txt");
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();


        PDFTextStripper s = new PDFTextStripper();
        String content = s.getText(PDDocument.load(serverFile));

        try( PrintWriter out = new PrintWriter(new File(root + File.separator + FilenameUtils.removeExtension(file.getOriginalFilename()) + ".txt"))){
            out.println(content);
        }
        String res = "";
        String [] string = content.split("\n");
        for(String ss:string ) {
            if (ss.contains("מספר חשבון חוזה")) {
                long l = Long.parseLong(ss.replaceAll("\\D", ""));
                System.out.println(l);
                bill.setBillNumber(l);

                res += ss + "\n";
                break;
            }
        }
        for(String ss:string ) {
            if (ss.contains("מ- ")) {
                bill.setDate(ss);
                res += ss + "\n";
                break;
            }
        }
        for(String ss:string ) {
            if (ss.contains("חסה\"כ לתשלום (ש\"ח)")) {
//                double d = Double.parseDouble(ss.replaceAll("\\D\\.", ""));
                String [] sss= ss.trim().split(" ");
                double d = Double.parseDouble(sss[0]);
                bill.setAmount(d);
//                ss.replaceAll("\\D", "");
                System.out.println(d);
                res += ss + "\n";
                break;
            }
        }


        //TODO: Create class Bill
        // create specific field for him
        //put all data to this class
        //return this object

//        String res = FileUtils.readFileToString(file);
//        PDDocument pdfFile;
//        PDFTextStripper stripper;
//        String text = null;
//        try {
//            pdfFile = PDDocument.load((File) file);
//            stripper = new PDFTextStripper();
//            text = stripper.getText(pdfFile);
//
//        } catch (IOException e) {
//            System.out.println("io error");
//            //Fix Logger
////            Logger logger = new Logger("Couldn't load the file");
//        }
//
//
//        System.out.println(res);
        return bill;
    }




}
