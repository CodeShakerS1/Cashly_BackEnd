package com.example.cashly_backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashly_backend.entity.Income;
import com.example.cashly_backend.service.IncomeService;

@RestController
public class IncomeController {

    @Autowired
    private IncomeService service;

    @GetMapping("/income")
    public ResponseEntity<List<Income>> getIncomes() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/income/{id}")
    public ResponseEntity<Income> getIncomeById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/income/user/{id}")
    public ResponseEntity<List<Income>> getIncomesByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByUser(id));
    }

    @GetMapping("/income/user/{id}/period")
    public ResponseEntity<List<Income>> getIncomesByPeriod(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.findByPeriod(id, start, end));
    }

    @GetMapping("/income/user/{id}/total")
    public ResponseEntity<BigDecimal> getTotalByPeriod(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.sumByPeriod(id, start, end));
    }

    @PostMapping("/income")
    public ResponseEntity<String> createIncome(@RequestBody Income income) {
        service.save(income);
        return ResponseEntity.status(201).body("Income created successfully!");
    }

    @PutMapping("/income/{id}")
    public ResponseEntity<String> updateIncome(@PathVariable Integer id, @RequestBody Income updatedIncome) {
        if (service.findById(id).isPresent()) {
            service.update(id, updatedIncome);
            return ResponseEntity.ok("Income updated successfully!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/income/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Integer id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}