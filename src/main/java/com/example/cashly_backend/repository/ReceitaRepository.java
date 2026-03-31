package com.example.cashly_backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cashly_backend.entity.Receita;

public interface ReceitaRepository extends JpaRepository<Receita,Integer>{

    @Query("SELECT r FROM Receita r WHERE r.usuario.id_usuario = :id")
    List<Receita> findByUsuarioId(@Param("id") Integer id);

    @Query("SELECT r FROM Receita r WHERE r.usuario.id_usuario = :id AND r.data BETWEEN :inicio AND :fim")
    List<Receita> findByUsuarioIdAndDataBetween(@Param("id") Integer id, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT SUM(r.valor) FROM Receita r WHERE r.usuario.id_usuario = :id AND r.data BETWEEN :inicio AND :fim")
    BigDecimal somarPorUsuarioEPeriodo(@Param("id") Integer id, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
