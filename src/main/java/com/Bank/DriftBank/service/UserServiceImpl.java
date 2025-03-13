package com.Bank.DriftBank.service;

import com.Bank.DriftBank.Utills.AccountUtills;
import com.Bank.DriftBank.dto.*;
import com.Bank.DriftBank.entity.Users;
import com.Bank.DriftBank.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements   UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    EmailServiceimpl emailServiceimpl;

    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(@NotNull UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtills.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtills.ACCOUNT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

            Users newUsers = Users.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .accountNumber(AccountUtills.generateAccountumber())
                    .accountBalance(BigDecimal.ZERO)
                    .email(userRequest.getEmail())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                    .status("ACTIVE")
                    .build();

            Users SavedUsers = userRepository.save(newUsers);
            EmailDetails emailDetails=EmailDetails.builder()
                    .recipient(SavedUsers.getEmail())
                    .subject("ACCOUNT CREATION")
                    .messageBody("Congratulation account has created .\n your ACcount Details : \n"+
                    "Account  name"+SavedUsers.getFirstName()+ " "+SavedUsers.getLastName()+" "+SavedUsers.getOtherName())

                    .build();
            emailService.sendEmailAlert(emailDetails);

            return BankResponse.builder()
                    .responseCode(AccountUtills.ACCOUNT_CREATION_SUCCES)
                    .responseMessage(AccountUtills.ACCOUNT_CREATION_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(SavedUsers.getAccountBalance())
                            .accountNumber(SavedUsers.getAccountNumber())
                            .accountNAme(SavedUsers.getFirstName() + " " + SavedUsers.getLastName() + " " + SavedUsers.getOtherName())

                            .build())
                    .build();


        }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {

        boolean isAccountnumberExist= userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountnumberExist){
return BankResponse.builder()
        .responseCode(AccountUtills.ACCOUNT_NOT_EXIST_CODE)
        .responseMessage(AccountUtills.ACCOUNT_NOT_EXIST_MESSAGE)
        .accountInfo(null)

        .build();

        }
        Users foundUser=userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return  BankResponse.builder()
                .responseCode(AccountUtills.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtills.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNAme(foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName())
                        .accountNumber(foundUser.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist=userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return AccountUtills.ACCOUNT_NOT_EXIST_MESSAGE;

        }
        Users founduser=userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return founduser.getFirstName()+" "+founduser.getLastName()+" "+founduser.getOtherName();


    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtills.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtills.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
            Users usertoCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
            usertoCredit.setAccountBalance(usertoCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
             userRepository.save(usertoCredit);

             //Save Transaction
        TransactionDto transactionDto=TransactionDto.builder()
                .accountNumber(usertoCredit.getAccountNumber())
                .amount(creditDebitRequest.getAmount())
                .transactionType("CREDIT")
                .build();
transactionService.saveTransaction(transactionDto);




            return BankResponse.builder()
                    .responseCode(AccountUtills.ACCOUNT_CREDITED_CODE)
                    .responseMessage(AccountUtills.ACCOUNT_CREDITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNAme(usertoCredit.getFirstName() + " " + usertoCredit.getLastName() + " " + usertoCredit.getOtherName())
                            .accountBalance(usertoCredit.getAccountBalance())
                            .accountNumber(usertoCredit.getAccountNumber())
                            .build())
                    .build();
        }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtills.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtills.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(null)


                    .build();
        }
        Users usertodebit=userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigDecimal availableBalance=usertodebit.getAccountBalance();
        BigDecimal debitAmount=creditDebitRequest.getAmount();
        if(availableBalance.compareTo(debitAmount)<0){
            return BankResponse.builder()
                    .responseCode(AccountUtills.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtills.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)

                    .build();
        }

        else {
            usertodebit.setAccountBalance(usertodebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(usertodebit);
            TransactionDto transactionDto=TransactionDto.builder()
                    .accountNumber(usertodebit.getAccountNumber())
                    .amount(creditDebitRequest.getAmount())
                    .transactionType("CREDIT")
                    .build();
            transactionService.saveTransaction(transactionDto);
            return BankResponse.builder()
                    .responseCode(AccountUtills.AMOUNT_DEBITED_CODE)
                    .responseMessage(AccountUtills.AMOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(creditDebitRequest.getAccountNumber())
                            .accountNAme(usertodebit.getFirstName()+" "+usertodebit.getLastName()+" "+ usertodebit.getOtherName())
                            .accountBalance(usertodebit.getAccountBalance())


                            .build())


                    .build();
        }

    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        boolean isDestinationAccountExists=userRepository.existsByAccountNumber(transferRequest.getDestinationAccount());
        if(!isDestinationAccountExists){
            return  BankResponse.builder()
                    .responseCode(AccountUtills.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtills.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)


                    .build();
        }
        Users SourceAccount=userRepository.findByAccountNumber(transferRequest.getSourceAccount());
        if(transferRequest.getAmount().compareTo(SourceAccount.getAccountBalance())>0){
            return  BankResponse.builder()
                    .responseCode(AccountUtills.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtills.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)


                    .build();
        }
        SourceAccount.setAccountBalance(SourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
        String SourceUserName=SourceAccount.getFirstName()+" "+SourceAccount.getLastName()+" "+SourceAccount.getOtherName();
        userRepository.save(SourceAccount);
        EmailDetails debitAlert=EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(SourceAccount.getEmail())
                .messageBody("The  Sum of "+transferRequest.getAmount()+"has be debit from your  current balnce is"+SourceAccount.getAccountBalance())

                .build();
        emailService.sendEmailAlert(debitAlert);

        Users destinationAccountUser=userRepository.findByAccountNumber(transferRequest.getDestinationAccount());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
       // String recipentName=destinationAccountUser.getFirstName()+" "+destinationAccountUser.getLastName()+" "+destinationAccountUser.getOtherName();
        userRepository.save(destinationAccountUser);
        EmailDetails CreditAlert=EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(SourceAccount.getEmail())
                .messageBody("The  Sum of "+transferRequest.getAmount()+"has be sent  from"+SourceUserName+"   the current balance is "+SourceAccount.getAccountBalance())

                .build();
        emailService.sendEmailAlert(CreditAlert);
        TransactionDto transactionDto=TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .amount(transferRequest.getAmount())
                .transactionType("CREDIT")
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtills.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtills.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();


    }


}
