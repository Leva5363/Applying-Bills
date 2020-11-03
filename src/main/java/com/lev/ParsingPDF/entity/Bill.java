package com.lev.ParsingPDF.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Bill {

    private String typeBill;

    @Id
    private long billNumber;

    private String fromDate;

    private String tillDate;

    private double amount;

}


