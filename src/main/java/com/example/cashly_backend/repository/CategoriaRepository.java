package com.example.cashly_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.cashly_backend.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByUsuarioIsNull();

    @Query("SELECT c FROM Categoria c WHERE c.usuario.id_usuario = :id")
    List<Categoria> findByUsuarioId(@Param("id") Integer id);
}