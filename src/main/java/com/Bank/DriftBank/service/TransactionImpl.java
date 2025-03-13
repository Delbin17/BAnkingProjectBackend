package com.Bank.DriftBank.service;

import com.Bank.DriftBank.dto.TransactionDto;
import com.Bank.DriftBank.entity.Transactions;
import com.Bank.DriftBank.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionImpl implements  TransactionService{
    @Autowired
    TransactionRepo transactionRepo;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transactions transactions=Transactions.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCES")
                .build();
        transactionRepo.save(transactions);
        System.out.println("Transcations Saved Succesfully");


    }
}
