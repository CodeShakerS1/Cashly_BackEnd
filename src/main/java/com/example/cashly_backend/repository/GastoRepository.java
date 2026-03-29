package com.example.cashly_backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cashly_backend.entity.Gasto;

public interface GastoRepository extends JpaRepository<Gasto, Integer> {

    @Query("SELECT g FROM Gasto g WHERE g.usuario.id_usuario = :id")
    List<Gasto> findByUsuarioId(@Param("id") Integer id);

    @Query("SELECT g FROM Gasto g WHERE g.usuario.id_usuario = :id AND g.data BETWEEN :inicio AND :fim")
    List<Gasto> findByUsuarioIdAndDataBetween(@Param("id") Integer id, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT SUM(g.valor) FROM Gasto g WHERE g.usuario.id_usuario = :id AND g.data BETWEEN :inicio AND :fim")
    BigDecimal somarPorUsuarioEPeriodo(@Param("id") Integer id, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}