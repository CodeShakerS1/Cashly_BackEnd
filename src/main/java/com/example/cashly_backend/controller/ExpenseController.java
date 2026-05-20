package com.example.cashly_backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashly_backend.entity.Expense;
import com.example.cashly_backend.service.ExpenseService;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @GetMapping("/expense")
    public ResponseEntity<List<Expense>> getExpenses() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/expense/user/{id}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByUser(id));
    }

    @GetMapping("/expense/user/{id}/period")
    public ResponseEntity<List<Expense>> getExpensesByPeriod(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.findByPeriod(id, start, end));
    }

    @GetMapping("/expense/user/{id}/total")
    public ResponseEntity<BigDecimal> getTotalByPeriod(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.sumByPeriod(id, start, end));
    }

    @GetMapping("/expense/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(
        @PathVariable Integer userId,
        @PathVariable Integer categoryId) {
    return ResponseEntity.ok(service.findByCategory(userId, categoryId));
    }

    @PostMapping("/expense")
    public ResponseEntity<String> createExpense(@RequestBody Expense expense) {
        service.save(expense);
        return ResponseEntity.status(201).body("Expense created successfully!");
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<String> updateExpense(@PathVariable Integer id, @RequestBody Expense updatedExpense) {
        if (service.findById(id).isPresent()) {
            service.update(id, updatedExpense);
            return ResponseEntity.ok("Expense updated successfully!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}