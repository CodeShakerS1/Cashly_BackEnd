package com.example.cashly_backend.dto;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private BigDecimal totalBalance;
    private BigDecimal weeklyExpensesTotal;
    private List<DailyExpense> weeklyChart;
    private List<CategoryExpense> categories;

    @Data
    @Builder
    public static class DailyExpense {
        private String day;     
        private BigDecimal total;
    }

    @Data
    @Builder
    public static class CategoryExpense {
        private String categoryName;
        private String icon;
        private BigDecimal total;
    }
}
