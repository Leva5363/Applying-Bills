package com.lev.ParsingPDF.entity;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@Builder
public class Bill {

    @Column(name = "billNumber")
    private long billNumber;

    @Column(name = "fromDate")
    private String fromDate;

    @Column(name = "tillDate")
    private String tillDate;

    @Column(name = "Amount")
    private double amount;

}


