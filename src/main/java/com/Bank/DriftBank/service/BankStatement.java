package com.Bank.DriftBank.service;


import com.Bank.DriftBank.dto.EmailDetails;
import com.Bank.DriftBank.entity.Transactions;
import com.Bank.DriftBank.entity.Users;
import com.Bank.DriftBank.repository.TransactionRepo;
import com.Bank.DriftBank.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class BankStatement {
    private TransactionRepo transactionRepo;
    private UserRepository userRepository;
    private EmailService emailService;

    //private static final String FILE="Desktop\\statement.pdf";
    private static final String FILE = System.getProperty("user.home") + "/Desktop/Statement.pdf";
    public List<Transactions> generateStatement(String accountNumber,String startDate,String endDate) throws FileNotFoundException, DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        LocalDate start=LocalDate.parse(startDate,formatter);
        LocalDate end=LocalDate.parse(endDate,formatter);
        List<Transactions> TranscationList= transactionRepo.findAll().stream()
                .filter(transactions -> transactions.getAccountNumber().equals(accountNumber))
                .filter(transactions -> transactions.getCreatedAt() != null)
                .filter(transactions ->
                        !transactions.getCreatedAt().isBefore(start) && !transactions.getCreatedAt().isAfter(end)
                )
                .collect(Collectors.toList());

        Users user=userRepository.findByAccountNumber(accountNumber);
        String customerName=user.getFirstName()+user.getLastName()+user.getOtherName();

        Rectangle statementSize=new Rectangle(PageSize.A4);
        Document document =new Document(statementSize);
        //log.info("Setting size of document");
        log.info("Saving PDF to: " + FILE);
        OutputStream outputStream=new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        PdfPTable bankInfoTable=new PdfPTable(1);
        PdfPCell bankName=new PdfPCell(new Phrase("The Drift Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress=new PdfPCell(new Phrase("65-A ,Fathimanager P O,KaniyaKumari TamilNadu"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo=new PdfPTable(2);
        PdfPCell customerInfo=new PdfPCell(new Phrase("Start date:"+startDate));
        customerInfo.setBorder(0);

        PdfPCell statment=new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statment.setBorder(0);
        PdfPCell stopDate=new PdfPCell(new Phrase("End Date"+endDate));
        stopDate.setBorder(0);


        PdfPCell name=new PdfPCell(new Phrase("Customer name :"+customerName));
        name.setBorder(0);

        PdfPCell space=new PdfPCell();
        space.setBorder(0);

        PdfPCell address=new PdfPCell(new Phrase("Customer Address:"+user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable=new PdfPTable(4);
        PdfPCell date=new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell transactionType=new PdfPCell(new Phrase("TransactionType"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);

        PdfPCell transactionAmount=new PdfPCell(new Phrase("TransactionAmount"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);

        PdfPCell status=new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);


        TranscationList.forEach(transactions -> {

            transactionTable.addCell(new Phrase(transactions.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transactions.getTransactionType()));
            transactionTable.addCell(new Phrase(transactions.getAmount().toString()));
            transactionTable.addCell(new Phrase(transactions.getStatus()));
        });
        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statment);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails=EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF BANK ACCOUNT")
                .messageBody("Kindly find your requested Bank statement attached")
                .attachment(FILE)
                .build();





        return TranscationList;
    }


}