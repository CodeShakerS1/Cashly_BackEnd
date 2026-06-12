package com.example.cashly_backend.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.User;
import com.example.cashly_backend.repository.DashboardRepository;
import com.example.cashly_backend.repository.UserRepository;

@Service
public class WeeklyReportService {

    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final NotificationService notificationService;

    WeeklyReportService(UserRepository userRepository,
                        DashboardRepository dashboardRepository,
                        NotificationService notificationService) {
        this.userRepository = userRepository;
        this.dashboardRepository = dashboardRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 12 * * MON")
    public void sendWeeklyReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfLastWeek = today.minusWeeks(1).with(DayOfWeek.SUNDAY);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);

        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Object[]> raw = dashboardRepository.getWeeklyExpenses(
                user.getId(), startOfLastWeek, endOfLastWeek);

            BigDecimal weeklyTotal = raw.stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal balance = dashboardRepository.getTotalBalance(user.getId());
            if (balance == null) balance = BigDecimal.ZERO;

            notificationService.createNotification(
                "📊 Resumo Semanal",
                "Na semana passada você gastou R$ " + weeklyTotal +
                ". Saldo atual: R$ " + balance,
                "weekly_report",
                user.getId()
            );
        }
    }
}