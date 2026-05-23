package com.example.cashly_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashly_backend.entity.Category;
import com.example.cashly_backend.service.CategoryService;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.registerCategory(category);
        return ResponseEntity.status(201).body("Category created successfully!");
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/default")
    public ResponseEntity<List<Category>> getDefaultCategories() {
        return ResponseEntity.ok(categoryService.findDefaultCategories());
    }

    @GetMapping("/category/user/{id}")
    public ResponseEntity<List<Category>> getCategoriesByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.findByUser(id));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Integer id, @RequestBody Category updatedCategory) {
        if (categoryService.findById(id).isPresent()) {
            categoryService.updateCategory(id, updatedCategory);
            return ResponseEntity.ok("Category updated successfully!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (categoryService.findById(id).isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}