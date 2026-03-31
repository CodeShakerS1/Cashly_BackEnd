package com.example.cashly_backend.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import com.example.cashly_backend.entity.Receita;
import com.example.cashly_backend.service.ReceitaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receita")
public class ReceitaController {

    @Autowired
    private ReceitaService service;

    @GetMapping
    public List<Receita> listarReceitas(){
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receita> listarReceita(@PathVariable Integer id) {
        Optional<Receita> receita = service.listarPorId(id);

        if (receita.isPresent()){
            return ResponseEntity.ok(receita.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{id}")
    public List<Receita> listarPorUsuario(@PathVariable Integer id){
        return service.listarPorUsuario(id);
    }

    @GetMapping("/usuario/{id}/periodo")
    public List<Receita> listarPorPeriodo(
        @PathVariable Integer id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim){
            return service.listarPorPeriodo(id, inicio, fim);
        }

        @PostMapping
        public Receita cadastrarReceita(@RequestBody Receita receita){
            return service.cadastrar(receita);
        }

        @PutMapping("/{id}")
        public Receita editarReceita(@PathVariable Integer id, @RequestBody Receita receita){
            return service.editar(id, receita);
        }

        @DeleteMapping("/{id}")
        public String deletarReceita(@PathVariable Integer id){
            service.deletar(id);
            return "Receita com ID: " + id + ", deletada!";
        }

}
