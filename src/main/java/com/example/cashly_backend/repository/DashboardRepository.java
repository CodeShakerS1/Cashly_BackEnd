package com.example.cashly_backend.repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cashly_backend.entity.Transaction;

public interface DashboardRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = """
        SELECT 
          (SELECT COALESCE(SUM(amount), 0) FROM income WHERE userid = :userId) -
          (SELECT COALESCE(SUM(amount), 0) FROM expense WHERE userid = :userId)
        """, nativeQuery = true)
    BigDecimal getTotalBalance(@Param("userId") Integer userId);

    @Query(value = """
        SELECT 
          DAYOFWEEK(t.transactiondate) AS dayOfWeek,
          COALESCE(SUM(t.amount), 0) AS total
        FROM transaction t
        WHERE t.userid = :userId
          AND t.expenseid IS NOT NULL
          AND t.transactiondate BETWEEN :startOfWeek AND :endOfWeek
        GROUP BY DAYOFWEEK(t.transactiondate)
        ORDER BY dayOfWeek
        """, nativeQuery = true)
    List<Object[]> getWeeklyExpenses(
        @Param("userId") Integer userId,
        @Param("startOfWeek") LocalDate startOfWeek,
        @Param("endOfWeek") LocalDate endOfWeek
    );

    @Query(value = """
        SELECT 
          c.categoryname,
          c.icon,
          COALESCE(SUM(t.amount), 0) AS total
        FROM category c
        LEFT JOIN expense e ON e.categoryid = c.categoryid AND e.userid = :userId
        LEFT JOIN transaction t ON t.expenseid = e.expenseid
        WHERE c.userid = :userId
        GROUP BY c.categoryid, c.categoryname, c.icon
        ORDER BY total DESC
        """, nativeQuery = true)
    List<Object[]> getAllCategories(@Param("userId") Integer userId);
}