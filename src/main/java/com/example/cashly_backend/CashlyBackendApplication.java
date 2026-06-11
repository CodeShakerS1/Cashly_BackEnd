package com.example.cashly_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class CashlyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashlyBackendApplication.class, args);
    }
}