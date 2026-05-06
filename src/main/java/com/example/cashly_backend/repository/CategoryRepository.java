package com.example.cashly_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.cashly_backend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByUserIdIsNull();

    @Query("SELECT c FROM Category c WHERE c.userId = :id")
    List<Category> findByUserId(@Param("id") Integer id);
}