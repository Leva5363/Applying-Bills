package com.lev.ParsingPDF;

import com.lev.ParsingPDF.service.ParsingPDFService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.Resource;

@SpringBootApplication
public class ParsingPdfApplication implements CommandLineRunner {
	@Resource
	ParsingPDFService parsingPDFService;

	public static void main(String[] args) {
		SpringApplication.run(ParsingPdfApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		parsingPDFService.init();
	}

}
