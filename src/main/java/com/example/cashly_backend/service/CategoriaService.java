package com.example.cashly_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Categoria;
import com.example.cashly_backend.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    public Optional<Categoria> listarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Categoria> listarPadrao() {
        return repository.findByUsuarioIsNull();
    }

    public List<Categoria> listarPorUsuario(Integer id) {
        return repository.findByUsuarioId(id);
    }

    public Categoria cadastrar(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria editar(Integer id, Categoria categoria) {
        categoria.setId_categoria(id);
        return repository.save(categoria);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}