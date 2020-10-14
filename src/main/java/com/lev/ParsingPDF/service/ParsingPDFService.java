package com.lev.ParsingPDF.service;

import com.lev.ParsingPDF.entity.Bill;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ParsingPDFService {

    Bill parsingFile(MultipartFile file) throws IOException;

    void init();

}
