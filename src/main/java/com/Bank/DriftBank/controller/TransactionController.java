package com.Bank.DriftBank.controller;


import com.Bank.DriftBank.entity.Transactions;
import com.Bank.DriftBank.service.BankStatement;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

    private BankStatement bankStatement;


    @GetMapping
    public List<Transactions> generateStatement(@RequestParam String accountNumber, @RequestParam String start, @RequestParam String end) throws DocumentException, FileNotFoundException {


        return bankStatement.generateStatement(accountNumber,start,end);
    }
}
