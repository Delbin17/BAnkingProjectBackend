package com.Bank.DriftBank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "The Drift Banking Application",
                description = "The Backend Api for Drift Bank application",
                version = "v1.0",
                contact = @Contact(
                        name = "Delbin",
                        email = "delbindeyohan@gmail.com",
                        url = "https://github.com/Delbin17/DriftBank.git"
                ),
                license = @License(
                        name = "DriftBanking",
                        url = "https://github.com/Delbin17/DriftBank.git"
                )

        ),
        externalDocs = @ExternalDocumentation
                (
                        description = "The Backend for Drift Banking",
                        url = "https://github.com/Delbin17/DriftBank.git"
                )

)
public class DriftBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriftBankApplication.class, args);
    }

}
