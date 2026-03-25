package com.example.cashly_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.cashly_backend.entity.Categoria;
import com.example.cashly_backend.repository.CategoriaRepository;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    @GetMapping
    public List<Categoria> listarCategorias() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> listarCategoria(@PathVariable Integer id) {
        Optional<Categoria> categoria = repository.findById(id);

        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Categoria cadastrarCategoria(@RequestBody @Valid Categoria categoria) {
        return repository.save(categoria);
    }

    @PutMapping("/{id}")
    public Categoria editarCategoria(@PathVariable Integer id, @RequestBody @Valid Categoria categoria) {
        categoria.setId_categoria(id);
        return repository.save(categoria);
    }

    @DeleteMapping("/{id}")
    public String deletarCategoria(@PathVariable Integer id) {
        repository.deleteById(id);
        return "Categoria com ID: " + id + ", deletada!";
    }
}