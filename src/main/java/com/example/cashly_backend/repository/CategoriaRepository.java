package com.example.cashly_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cashly_backend.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}