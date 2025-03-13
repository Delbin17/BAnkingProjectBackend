package com.Bank.DriftBank.repository;

import com.Bank.DriftBank.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transactions, String> {


    @Query("SELECT t FROM Transactions t WHERE t.accountNumber = :accountNumber AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transactions> findByAccountNumberAndCreatedAtBetween(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

