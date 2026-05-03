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
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<String> criarCategoria(@RequestBody Category category) {
        categoryService.cadastrar(category);
        return ResponseEntity.status(201).body("Categoria criada com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategory() {
        List<Category> categorias = categoryService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/padrao")
    public ResponseEntity<List<Category>> getCategoryPadrao() {
        return ResponseEntity.ok(categoryService.listarPadrao());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Category>> getCategoryByUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.listarPorUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {

        if (categoryService.listarPorId(id).isPresent()) {
            categoryService.editar(id, updatedCategory);
            return ResponseEntity.ok("Categoria atualizada com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.listarPorId(id).isPresent()) {
            categoryService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}