package com.example.cashly_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionid")
    private Integer id;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "method", length = 50)
    private String method;

    @Column(name = "transactiondate", nullable = false)
    private LocalDate date;

    @Column(name = "userid", nullable = false)
    private Integer userId;
 
    @Column(name = "expenseid")
    private Integer expenseId;
 
    @Column(name = "incomeid")
    private Integer incomeId;

    public Transaction(Integer id, BigDecimal amount, String description, String method, LocalDate date, Integer userId, Integer expenseId, Integer incomeId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.method = method;
        this.date = date;
        this.userId = userId;
        this.expenseId = expenseId;
        this.incomeId = incomeId;
    }
 
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
 
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
 
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
 
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
 
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
 
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
 
    public Integer getExpenseId() { return expenseId; }
    public void setExpenseId(Integer expenseId) { this.expenseId = expenseId; }
 
    public Integer getIncomeId() { return incomeId; }
    public void setIncomeId(Integer incomeId) { this.incomeId = incomeId; }
}

