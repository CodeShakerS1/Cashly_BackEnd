package com.example.cashly_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Gasto;
import com.example.cashly_backend.repository.GastoRepository;

@Service
public class GastoService {

    @Autowired
    private GastoRepository repository;

    public List<Gasto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Gasto> listarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Gasto> listarPorUsuario(Integer id) {
        return repository.findByUsuarioId(id);
    }

    public List<Gasto> listarPorPeriodo(Integer id, LocalDate inicio, LocalDate fim) {
        return repository.findByUsuarioIdAndDataBetween(id, inicio, fim);
    }

    public BigDecimal somarPorPeriodo(Integer id, LocalDate inicio, LocalDate fim) {
        BigDecimal total = repository.somarPorUsuarioEPeriodo(id, inicio, fim);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Gasto cadastrar(Gasto gasto) {
        return repository.save(gasto);
    }

    public Gasto editar(Integer id, Gasto gasto) {
        gasto.setId_gasto(id);
        return repository.save(gasto);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}