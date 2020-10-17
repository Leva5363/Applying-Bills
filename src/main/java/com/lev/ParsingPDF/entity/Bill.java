package com.lev.ParsingPDF.entity;


import javax.persistence.*;
import java.sql.Date;

@Entity
public class Bill {
    
    //TODO lombock : reduce code
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "billNumber")
    private long billNumber;

    @Column(name = "date")
    private String date;

    @Column(name = "Amount")
    private double amount;

    public Bill() {
    }



    public Bill(long billNumber, String date, double amount) {
        this.billNumber = billNumber;
        this.date = date;
        this.amount = amount;
    }

    public long getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(long billNumber) {
        this.billNumber = billNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", billNumber=" + billNumber +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }
}


