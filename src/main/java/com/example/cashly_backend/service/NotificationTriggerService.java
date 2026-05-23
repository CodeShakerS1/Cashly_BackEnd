package com.example.cashly_backend.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.repository.ExpenseRepository;
import com.example.cashly_backend.entity.Category;

@Service
public class NotificationTriggerService {

    @Autowired
    public NotificationService notificationService;

    @Autowired
    public ExpenseRepository expenseRepository;

    public void notifyIncomeAdded(String incomeName,  BigDecimal amount, Integer userId){
        notificationService.createNotification(
            " ✅ Receita adicionada!",
            "Você adicionou uma receita de R$ " + amount + " (" + incomeName +")",
            "income_sucess",
            userId
        );
    }
    
    
    public void checkCategoryLimit(Category category, Integer userId){
        if (categoria.getLimitAmount() == null){
            return;
        }

        BigDecimal totalExpense = expenseRepository.sumByUserIdAndCategoryIdCurrentMonth(userId, category.getCategoryid());
        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }

        BigDecimal limit = category.getLimitAmount();

        BigDecimal percentageSpent = totalExpense.divide(limit, 2, java.math.RoundingMode.HALF_UP)
                                              .multiply(new BigDecimal("100"));

        if (percentageSpent.compareTo(new BigDecimal("80")) >= 0 && 
            percentageSpent.compareTo(new BigDecimal("100")) < 0) {
            
            notificationService.createNotification(
                "⚠️ Limite em Alerta",
                "Você atingiu 80% do limite de " + categoria.getCategoryname() + 
                " (R$ " + totalExpense + " de R$ " + limit + ")",
                "limit_warning_80",
                userId
            );
        }

        else if (percentageSpent.compareTo(new BigDecimal("100")) >= 0) {
            notificationService.createNotification(
                "🚨 Limite Excedido",
                "Você excedeu o limite de " + categoria.getCategoryname() + 
                " (R$ " + totalGasto + " de R$ " + limite + ")",
                "limit_exceeded_100",
                userId
            );
        }
    }
}
