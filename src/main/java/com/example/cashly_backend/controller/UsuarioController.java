package com.example.cashly_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cashly_backend.entity.Usuario;
import com.example.cashly_backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;
     
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario usuario) {
        return repository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario editarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario){
        usuario.setId_usuario(id);
        return repository.save(usuario);
    }
    @DeleteMapping("/{id}")
    public String deletarUsuario(@PathVariable Integer id) {
        repository.deleteById(id);
        return "Usuario com ID: " + id + ", deletado!";
        
    }
}
