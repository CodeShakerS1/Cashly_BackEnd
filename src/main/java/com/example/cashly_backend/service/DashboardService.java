package com.example.cashly_backend.service;

import com.example.cashly_backend.dto.DashboardResponse;
import com.example.cashly_backend.repository.DashboardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    private static final String[] DAY_NAMES = {"D", "S", "T", "Q", "Q", "S", "S"};

    public DashboardResponse getDashboard(Integer userId, String week) {
        LocalDate today = LocalDate.now();

        LocalDate currentStart = today.with(DayOfWeek.MONDAY).minusDays(1);
        LocalDate currentEnd = currentStart.plusDays(6);

        LocalDate chartStart = currentStart;
        LocalDate chartEnd = currentEnd;

        if ("previous".equals(week)) {
            chartStart = currentStart.minusWeeks(1);
            chartEnd = currentEnd.minusWeeks(1);
        }

        BigDecimal totalBalance = dashboardRepository.getTotalBalance(userId);

        List<Object[]> rawChart = dashboardRepository.getWeeklyExpenses(userId, chartStart, chartEnd);
        Map<Integer, BigDecimal> chartMap = new HashMap<>();
        for (Object[] row : rawChart) {
            chartMap.put(((Number) row[0]).intValue(), (BigDecimal) row[1]);
        }

        List<DashboardResponse.DailyExpense> weeklyChart = new ArrayList<>();
        BigDecimal weeklyTotal = BigDecimal.ZERO;
        for (int i = 1; i <= 7; i++) {
            BigDecimal dayTotal = chartMap.getOrDefault(i, BigDecimal.ZERO);
            weeklyTotal = weeklyTotal.add(dayTotal);
            weeklyChart.add(DashboardResponse.DailyExpense.builder()
                    .day(DAY_NAMES[i - 1])
                    .total(dayTotal)
                    .build());
        }

        List<Object[]> rawCategories = dashboardRepository.getAllCategories(userId);
       List<DashboardResponse.CategoryExpense> categories = rawCategories.stream()
        .map(row -> DashboardResponse.CategoryExpense.builder()
                .categoryId(((Number) row[0]).intValue())
                .categoryName((String) row[1])
                .icon((String) row[2])
                .limitAmount((BigDecimal) row[3])
                .total((BigDecimal) row[4])
                .build())
        .toList();

        return DashboardResponse.builder()
                .totalBalance(totalBalance)
                .weeklyExpensesTotal(weeklyTotal)
                .weeklyChart(weeklyChart)
                .categories(categories)
                .build();
    }
}