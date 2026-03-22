package com.example.cashly_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
@RequestMapping("/contato")
public class CashlyBackendApplication {
    
    @GetMapping
    public String Get() {
        return "GET funcionando!";
    }

    @PostMapping
    public String Post() {
        return "POST funcionando!";
    }

    @PutMapping
    public String Put() {
        return "PUT funcionando!";
    }

    @DeleteMapping
    public String Delete() {
        return "DELETE funcionando!";
    }

    public static void main(String[] args) {
        SpringApplication.run(CashlyBackendApplication.class, args);
    }
}