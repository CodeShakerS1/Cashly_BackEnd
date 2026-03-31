package com.example.cashly_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Receita;
import com.example.cashly_backend.repository.ReceitaRepository;

@Service
public class ReceitaService {
    @Autowired
    private ReceitaRepository repository;

    public List<Receita> listarTodos(){
        return repository.findAll();
    }

    public Optional<Receita> listarPorId(Integer id){
        return repository.findById(id);
    }

    public List<Receita> listarPorUsuario(Integer id){
        return repository.findByUsuarioId(id);
    }

    public List<Receita> listarPorPeriodo(Integer id, LocalDate inicio, LocalDate fim){
        return repository.findByUsuarioIdAndDataBetween(id, inicio, fim);
    }

    public BigDecimal somarPorPeriodo(Integer id, LocalDate inicio, LocalDate fim){
        BigDecimal total = repository.somarPorUsuarioEPeriodo(id,inicio,fim);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Receita cadastrar(Receita receita){
        return repository.save(receita);
    }

    public Receita editar(Integer id, Receita receita){
        receita.setId_receita(id);
        return repository.save(receita);
    }

    public void deletar(Integer id){
        repository.deleteById(id);
    }

}
