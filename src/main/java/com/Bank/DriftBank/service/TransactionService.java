package com.Bank.DriftBank.service;

import com.Bank.DriftBank.dto.TransactionDto;
import com.Bank.DriftBank.entity.Transactions;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);
}
