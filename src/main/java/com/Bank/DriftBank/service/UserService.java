package com.Bank.DriftBank.service;

import com.Bank.DriftBank.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

      String nameEnquiry(EnquiryRequest enquiryRequest);

      BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

      BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

      BankResponse transfer(TransferRequest transferRequest);

}
