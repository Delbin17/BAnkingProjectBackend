package com.Bank.DriftBank.service;

import com.Bank.DriftBank.dto.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}
