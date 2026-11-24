package com.example.cashly_backend.controller;

import java.io.IOException;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cashly_backend.service.SpreadsheetService;

@RestController
public class SpreadsheetController {

    private final SpreadsheetService service;

    SpreadsheetController(SpreadsheetService service) {
        this.service = service;
    }


    @PostMapping("/expense/import")
    public ResponseEntity<?> importExpenses(
            @RequestParam("file") MultipartFile file,
            @RequestParam Integer userId) {
        try {
            return ResponseEntity.ok(service.importExpenses(file, userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Não foi possível ler o arquivo.");
        }
    }


    @GetMapping("/expense/user/{id}/export")
    public ResponseEntity<byte[]> exportExpenses(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "xlsx") String format) throws IOException {

        boolean csv = format.equalsIgnoreCase("csv");
        byte[] content = service.exportExpenses(id, format);

        MediaType type = csv
                ? MediaType.parseMediaType("text/csv; charset=UTF-8")
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(csv ? "despesas.csv" : "despesas.xlsx").build().toString())
                .contentType(type)
                .body(content);
    }
}