package com.example.cashly_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashly_backend.entity.User;
import com.example.cashly_backend.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        repository.save(user);
        return ResponseEntity.status(201).body("User created successfully!");
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return repository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhoto(updatedUser.getPhoto());
            repository.save(user);
            return ResponseEntity.ok("User updated successfully!");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        return repository.findByEmail(credentials.get("email"))
                .filter(u -> u.getPassword().equals(credentials.get("password")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}