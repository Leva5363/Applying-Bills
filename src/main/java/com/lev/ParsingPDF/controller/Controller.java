package com.lev.ParsingPDF.controller;

import com.lev.ParsingPDF.entity.Bill;
import com.lev.ParsingPDF.service.ParsingPDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {

    //TODO amount of all bills, Make security for access(create user+token for him, base64)
    //TODO Spring web security


    private final ParsingPDFService parsingPDFService;
    
    @PostMapping("/file")
    public Bill fetchingInfoFromFile(@RequestBody MultipartFile file) throws IOException {
        return parsingPDFService.fetchingInfoFromFile(file);
    }

    @PostMapping("/files")
    public List<Bill> fetchingInfoFromFiles(@RequestParam("files") MultipartFile[] files) {
        ArrayList<Bill> listOfBills = new ArrayList<>();
        try {
            Arrays.asList(files).stream().forEach(file -> {
                try {
                    listOfBills.add(parsingPDFService.fetchingInfoFromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfBills;
    }

    @GetMapping("/getBillById")
    public Bill getBillById(@RequestParam long id){
        return parsingPDFService.getBillById(id);
    }

    @GetMapping("/getAllByType")
    public List<Bill> getAllByType(@RequestParam String type){
        return parsingPDFService.getAllByType(type);
    }

    @GetMapping("/getAmountOfAllBills")
    public Double getAmountOfAllBills(){
        return parsingPDFService.getAmountOfAllBills();
    }
}
