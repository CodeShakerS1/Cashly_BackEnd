package com.example.cashly_backend.dto;

import java.util.List;

public record ImportResult(int imported, List<String> errors) {}