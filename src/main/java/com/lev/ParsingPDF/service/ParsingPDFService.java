package com.lev.ParsingPDF.service;

import com.lev.ParsingPDF.entity.Bill;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ParsingPDFService {

    Bill fetchingInfoFromFile(MultipartFile file) throws IOException;

    void init();

    Bill getBillById(long id);

    List<Bill> getAllByType(String type);

    Double getAmountOfAllBills();
}
