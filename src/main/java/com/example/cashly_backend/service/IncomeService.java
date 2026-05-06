package com.example.cashly_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Income;
import com.example.cashly_backend.repository.IncomeRepository;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository repository;

    public List<Income> findAll() {
        return repository.findAll();
    }

    public Optional<Income> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Income> findByUser(Integer id) {
        return repository.findByUserId(id);
    }

    public List<Income> findByPeriod(Integer id, LocalDate start, LocalDate end) {
        return repository.findByUserIdAndDateBetween(id, start, end);
    }

    public BigDecimal sumByPeriod(Integer id, LocalDate start, LocalDate end) {
        BigDecimal total = repository.sumByUserAndPeriod(id, start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Income save(Income income) {
        return repository.save(income);
    }

    public Income update(Integer id, Income income) {
        income.setId(id);
        return repository.save(income);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}