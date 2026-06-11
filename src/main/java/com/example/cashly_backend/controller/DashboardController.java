package com.example.cashly_backend.controller;

import com.example.cashly_backend.dto.DashboardResponse;
import com.example.cashly_backend.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/dashboard")
    public class DashboardController {

        private final DashboardService dashboardService;

        DashboardController(DashboardService dashboardService) {
            this.dashboardService = dashboardService;
        }

    @GetMapping("/{userId}")
        public ResponseEntity<DashboardResponse> getDashboard(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "current") String week) {
        return ResponseEntity.ok(dashboardService.getDashboard(userId, week));
    }
    }