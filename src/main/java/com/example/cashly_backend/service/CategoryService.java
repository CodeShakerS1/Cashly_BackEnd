package com.example.cashly_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Category;
import com.example.cashly_backend.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> listarTodas() {
        return repository.findAll();
    }

    public Optional<Category> listarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Category> listarPadrao() {
        return repository.findByUserIdIsNull();
    }

    public List<Category> listarPorUsuario(Integer id) {
        return repository.findByUserId(id);
    }

    public Category cadastrar(Category category) {
        return repository.save(category);
    }

    public Category editar(Integer id, Category category) {
        category.setCategoryid(id);
        return repository.save(category);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}