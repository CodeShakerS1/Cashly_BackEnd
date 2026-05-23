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

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Category> findDefaultCategories() {
        return repository.findByUserIdIsNull();
    }

    public List<Category> findByUser(Integer id) {
        return repository.findByUserId(id);
    }

    public Category registerCategory(Category category) {
        return repository.save(category);
    }

    public Category updateCategory(Integer id, Category category) {
        category.setCategoryid(id);
        return repository.save(category);
    }

    public void deleteCategory(Integer id) {
        repository.deleteById(id);
    }
}