package com.example.cashly_backend.service;

import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Transaction;
import com.example.cashly_backend.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Optional<Transaction> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Transaction> findByUser(Integer id){
        return repository.findByUserId(id);
    }

    public List<Transaction> findByPeriod(Integer id, LocalDate start, LocalDate end){
        return repository.findByUserIdAndDateBetween(id, null, null);
    }

    public Transaction save(Transaction transaction){
        return repository.save(transaction);
    }

    public Transaction update(Integer id, Transaction transaction){
        transaction.setId(id);
        return repository.save(transaction);
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }


}
