package com.lev.ParsingPDF.repository;

import com.lev.ParsingPDF.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
