package com.example.cashly_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenseid")
    private Integer id;

    @Column(name = "expensename", length = 100, nullable = false)
    private String name;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "method", length = 50, nullable = false)
    private String method;

    @Column(name = "expensedate", nullable = false)
    private LocalDate date;

    @Column(name = "userid")
    private Integer userId;

    @Column(name = "categoryid")
    private Integer categoryId;

    public Expense() {}

    public Expense(Integer id, String name, BigDecimal amount, String method, LocalDate date, Integer userId, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.method = method;
        this.date = date;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
}