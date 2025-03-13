package com.Bank.DriftBank.Utills;

import java.time.Year;

public class AccountUtills {

    public static final String ACCOUNT_EXIST_CODE="001";
    public static final String ACCOUNT_EXIST_MESSAGE="this User already existed";

    public static final String ACCOUNT_CREATION_SUCCES="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account Created Succesfully";

    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="this Account  not existed";

    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_MESSAGE="this Account  existed";


    public static final String ACCOUNT_CREDITED_CODE="005";
    public static final String ACCOUNT_CREDITED_MESSAGE="this Account  existed";

    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="insufficiebt balance to withdraw";

    public static final String AMOUNT_DEBITED_CODE="007";
    public static final String AMOUNT_DEBITED_MESSAGE="Amount  debited SUccesfully";

    public static final String TRANSFER_SUCCESS_CODE="008";
    public static final String TRANSFER_SUCCESS_MESSAGE="Transfer SUccesfully";



    public static String generateAccountumber() {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year=String.valueOf(currentYear);
        String randomNumber=String.valueOf(randNumber);
        return year + randomNumber;
    }
}