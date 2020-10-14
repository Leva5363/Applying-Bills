package com.lev.ParsingPDF.controller;

import com.lev.ParsingPDF.ParsingPdfApplication;
import com.lev.ParsingPDF.entity.Bill;
import com.lev.ParsingPDF.service.ParsingPDFService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    ParsingPDFService parsingPDFService;

    //TODO Fix return of the method

    @PostMapping("/file")
    public Bill getFile(@RequestBody MultipartFile file) throws IOException {
        return parsingPDFService.parsingFile(file);
    }

    @PostMapping("/upload")
    public List<Bill> getFile(@RequestParam("files") MultipartFile[] files) {
        ArrayList<Bill> listOfBills = new ArrayList<>();
        try {
            Arrays.asList(files).stream().forEach(file -> {
                try {
                    listOfBills.add(parsingPDFService.parsingFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfBills;
    }
}
