package com.example.cashly_backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashly_backend.entity.Gasto;
import com.example.cashly_backend.service.GastoService;

@RestController
@RequestMapping("/gasto")
public class GastoController {

    @Autowired
    private GastoService service;

    @GetMapping
    public List<Gasto> listarGastos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gasto> listarGasto(@PathVariable Integer id) {
        Optional<Gasto> gasto = service.listarPorId(id);

        if (gasto.isPresent()) {
            return ResponseEntity.ok(gasto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{id}")
    public List<Gasto> listarPorUsuario(@PathVariable Integer id) {
        return service.listarPorUsuario(id);
    }

    @GetMapping("/usuario/{id}/periodo")
    public List<Gasto> listarPorPeriodo(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.listarPorPeriodo(id, inicio, fim);
    }

    @GetMapping("/usuario/{id}/total")
    public BigDecimal totalPorPeriodo(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.somarPorPeriodo(id, inicio, fim);
    }

    @PostMapping
    public Gasto cadastrarGasto(@RequestBody Gasto gasto) {
        return service.cadastrar(gasto);
    }

    @PutMapping("/{id}")
    public Gasto editarGasto(@PathVariable Integer id, @RequestBody Gasto gasto) {
        return service.editar(id, gasto);
    }

    @DeleteMapping("/{id}")
    public String deletarGasto(@PathVariable Integer id) {
        service.deletar(id);
        return "Gasto com ID: " + id + ", deletado!";
    }
}