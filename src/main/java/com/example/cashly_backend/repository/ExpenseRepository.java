    package com.example.cashly_backend.repository;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.util.List;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import com.example.cashly_backend.entity.Expense;

    public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
        

        @Query("SELECT e FROM Expense e WHERE e.userId = :id")
        List<Expense> findByUserId(@Param("id") Integer id);

        @Query("SELECT e FROM Expense e WHERE e.userId = :id AND e.date BETWEEN :start AND :end")
        List<Expense> findByUserIdAndDateBetween(@Param("id") Integer id, @Param("start") LocalDate start, @Param("end") LocalDate end);

        @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :id AND e.date BETWEEN :start AND :end")
        BigDecimal sumByUserAndPeriod(@Param("id") Integer id, @Param("start") LocalDate start, @Param("end") LocalDate end);

        @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.categoryId = :categoryId")
        List<Expense> findByUserIdAndCategoryId(@Param("userId") Integer userId, @Param("categoryId") Integer categoryId);

        @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :userId AND e.categoryId = :categoryId AND MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE)")
        BigDecimal sumByUserIdAndCategoryIdCurrentMonth(@Param("userId") Integer userId, @Param("categoryId") Integer categoryId);
        @Query(value = """
    SELECT c.categoryname, COALESCE(SUM(e.amount), 0)
    FROM category c
    LEFT JOIN expense e ON e.categoryid = c.categoryid 
        AND e.userid = :userId
        AND e.expensedate BETWEEN :start AND :end
    WHERE c.userid = :userId
    GROUP BY c.categoryname
    """, nativeQuery = true)
List<Object[]> sumByCategoryAndPeriod(
    @Param("userId") Integer userId,
    @Param("start") LocalDate start,
    @Param("end") LocalDate end
);
    }