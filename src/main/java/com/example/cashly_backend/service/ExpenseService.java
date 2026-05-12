package com.example.cashly_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Expense;
import com.example.cashly_backend.entity.Transaction;
import com.example.cashly_backend.repository.ExpenseRepository;
import com.example.cashly_backend.repository.TransactionRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Expense> findAll() {
        return repository.findAll();
    }

    public Optional<Expense> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Expense> findByUser(Integer id) {
        return repository.findByUserId(id);
    }

    public List<Expense> findByPeriod(Integer id, LocalDate start, LocalDate end) {
        return repository.findByUserIdAndDateBetween(id, start, end);
    }

    public BigDecimal sumByPeriod(Integer id, LocalDate start, LocalDate end) {
        BigDecimal total = repository.sumByUserAndPeriod(id, start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

  public Expense save(Expense expense) {
    Expense saved = repository.save(expense);

    Transaction transaction = new Transaction();
    transaction.setAmount(saved.getAmount());
    transaction.setDescription(saved.getName());      // getName() não getExpensename()
    transaction.setMethod(saved.getMethod());
    transaction.setDate(saved.getDate());             // getDate() não getExpensedate()
    transaction.setUserId(saved.getUserId());         // getUserId() não getUserid()
    transaction.setExpenseId(saved.getId());          // getId() não getExpenseid()

    transactionRepository.save(transaction);

    return saved;
}

    public Expense update(Integer id, Expense expense) {
        expense.setId(id);
        return repository.save(expense);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}