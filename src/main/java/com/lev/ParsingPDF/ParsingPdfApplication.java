package com.lev.ParsingPDF;

import com.lev.ParsingPDF.service.ParsingPDFService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ParsingPdfApplication implements CommandLineRunner {

	private final ParsingPDFService parsingPDFService;

	public static void main(String[] args) {
		SpringApplication.run(ParsingPdfApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		parsingPDFService.init();
	}

}

