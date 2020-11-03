package com.lev.ParsingPDF.repository;

import com.lev.ParsingPDF.entity.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends MongoRepository<Bill, Long> {

    Bill save(Bill bill);

    List<Bill> findAllByTypeBill(String type);
    @Query("{ $sum: <amount> }")
    Double findAllAmount();
}
