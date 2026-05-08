package com.example.cashly_backend.repository;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.cashly_backend.entity.Transaction;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.userId = :id")
    List<Transaction> findByUserId(@Param("id") Integer id);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :id AND t.date BETWEEN :start AND :end")
    List<Transaction> findByUserIdAndDateBetween(@Param("id") Integer id, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
