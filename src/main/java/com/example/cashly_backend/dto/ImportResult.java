package com.example.cashly_backend.dto;

import java.util.List;

// Resposta da importação: quantas linhas entraram, quais deram erro e quais categorias foram criadas
public record ImportResult(int imported, List<String> errors, List<String> createdCategories) {}