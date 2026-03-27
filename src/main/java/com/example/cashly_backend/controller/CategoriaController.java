package com.example.cashly_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.cashly_backend.entity.Categoria;
import com.example.cashly_backend.service.CategoriaService;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public List<Categoria> listarCategorias() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> listarCategoria(@PathVariable Integer id) {
        Optional<Categoria> categoria = service.listarPorId(id);

        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/padrao")
    public List<Categoria> listarPadrao() {
        return service.listarPadrao();
    }

    @GetMapping("/usuario/{id}")
    public List<Categoria> listarPorUsuario(@PathVariable Integer id) {
        return service.listarPorUsuario(id);
    }

    @PostMapping
    public Categoria cadastrarCategoria(@RequestBody @Valid Categoria categoria) {
        return service.cadastrar(categoria);
    }

    @PutMapping("/{id}")
    public Categoria editarCategoria(@PathVariable Integer id, @RequestBody @Valid Categoria categoria) {
        return service.editar(id, categoria);
    }

    @DeleteMapping("/{id}")
    public String deletarCategoria(@PathVariable Integer id) {
        service.deletar(id);
        return "Categoria com ID: " + id + ", deletada!";
    }
}