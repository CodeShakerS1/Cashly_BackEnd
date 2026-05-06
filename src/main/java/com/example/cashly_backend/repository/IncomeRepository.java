package com.example.cashly_backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cashly_backend.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    @Query("SELECT i FROM Income i WHERE i.userId = :id")
    List<Income> findByUserId(@Param("id") Integer id);

    @Query("SELECT i FROM Income i WHERE i.userId = :id AND i.date BETWEEN :start AND :end")
    List<Income> findByUserIdAndDateBetween(@Param("id") Integer id, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.userId = :id AND i.date BETWEEN :start AND :end")
    BigDecimal sumByUserAndPeriod(@Param("id") Integer id, @Param("start") LocalDate start, @Param("end") LocalDate end);
}