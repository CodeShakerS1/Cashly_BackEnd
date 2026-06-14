    package com.example.cashly_backend.service;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.text.NumberFormat;
    import java.time.DayOfWeek;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;
    import java.util.Set;

    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Service;

    import com.example.cashly_backend.entity.User;
    import com.example.cashly_backend.repository.DashboardRepository;
    import com.example.cashly_backend.repository.ExpenseRepository;
    import com.example.cashly_backend.repository.UserRepository;

    @Service
    public class WeeklyReportService {

        private final UserRepository userRepository;
        private final DashboardRepository dashboardRepository;
        private final NotificationService notificationService;
        private final ExpenseRepository expenseRepository;

        WeeklyReportService(UserRepository userRepository,
                            DashboardRepository dashboardRepository,
                            NotificationService notificationService,
                            ExpenseRepository expenseRepository) {
            this.userRepository = userRepository;
            this.dashboardRepository = dashboardRepository;
            this.notificationService = notificationService;
            this.expenseRepository = expenseRepository;
        }

       @Scheduled(cron = "0 0 12 * * MON")
        public void sendWeeklyReport() {
            LocalDate today = LocalDate.now();

            LocalDate lastWeekStart = today.minusWeeks(1).with(DayOfWeek.MONDAY);
            LocalDate lastWeekEnd = lastWeekStart.plusDays(6);

            LocalDate prevWeekStart = lastWeekStart.minusWeeks(1);
            LocalDate prevWeekEnd = prevWeekStart.plusDays(6);

            List<User> users = userRepository.findAll();

            for (User user : users) {
                List<Object[]> lastWeek = expenseRepository.sumByCategoryAndPeriod(user.getId(), lastWeekStart, lastWeekEnd);
                List<Object[]> prevWeek = expenseRepository.sumByCategoryAndPeriod(user.getId(), prevWeekStart, prevWeekEnd);
                Map<String, BigDecimal> lastMap = toMap(lastWeek);
                Map<String, BigDecimal> prevMap = toMap(prevWeek);
                BigDecimal balance = dashboardRepository.getTotalBalance(user.getId());
                if (balance == null) balance = BigDecimal.ZERO;

                String message = buildCategoryComparison(
        lastMap,
        prevMap,
        balance,
        lastWeekStart,
        lastWeekEnd
    );

                notificationService.createNotification(
                    "📊 Resumo Semanal",
                    message,
                    "weekly_report",
                    user.getId()
                );
            }
        }

        private Map<String, BigDecimal> toMap(List<Object[]> rows) {
            Map<String, BigDecimal> map = new HashMap<>();
            for (Object[] row : rows) {
                map.put((String) row[0], (BigDecimal) row[1]);
            }
            return map;
        }

    private String buildCategoryComparison(
            Map<String, BigDecimal> last,
            Map<String, BigDecimal> prev,
            BigDecimal balance,
            LocalDate periodStart,
            LocalDate periodEnd) {

    NumberFormat currency =
        NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

        StringBuilder msg = new StringBuilder();

        msg.append("📊 Resumo Semanal (")
        .append(periodStart.format(DateTimeFormatter.ofPattern("dd/MM")))
        .append(" - ")
        .append(periodEnd.format(DateTimeFormatter.ofPattern("dd/MM")))
        .append(")\n\n");

        msg.append("Saldo atual: ")
        .append(currency.format(balance))
        .append(".\n\n");

        boolean hasComparison = false;

        Set<String> allCategories = new HashSet<>();
        allCategories.addAll(last.keySet());
        allCategories.addAll(prev.keySet());

        for (String category : allCategories) {

            BigDecimal lastTotal = last.getOrDefault(category, BigDecimal.ZERO);
            BigDecimal prevTotal = prev.getOrDefault(category, BigDecimal.ZERO);

            if (prevTotal.compareTo(BigDecimal.ZERO) == 0) {

                if (lastTotal.compareTo(BigDecimal.ZERO) > 0) {
                    msg.append("🆕 ")
                    .append(category)
                    .append(": novos gastos registrados nesta semana (")
                    .append(currency.format(lastTotal))
                    .append(").\n");

                    hasComparison = true;
                }

                continue;
            }

            BigDecimal diff = lastTotal.subtract(prevTotal)
                    .divide(prevTotal, 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));

            if (diff.compareTo(new BigDecimal("-10")) <= 0) {

                msg.append("✅ ")
                .append(category)
                .append(": você economizou ")
                .append(diff.abs())
                .append("% em relação à semana passada.\n");

                hasComparison = true;

            } else if (diff.compareTo(new BigDecimal("500")) >= 0) {

                msg.append("⚠️ ")
                .append(category)
                .append(": seus gastos passaram de ")
                .append(currency.format(prevTotal))
                .append(" para ")
                .append(currency.format(lastTotal))
                .append(".\n");

                hasComparison = true;

            } else if (diff.compareTo(new BigDecimal("10")) >= 0) {

                msg.append("⚠️ ")
                .append(category)
                .append(": você gastou ")
                .append(diff)
                .append("% a mais que na semana passada.\n");

                hasComparison = true;
            }
        }

        if (!hasComparison) {
            msg.append("👏 Seus gastos permaneceram estáveis nesta semana. Continue assim!");
        }

        return msg.toString();
    }
    }