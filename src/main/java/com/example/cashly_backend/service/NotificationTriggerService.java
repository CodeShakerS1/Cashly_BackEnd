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

    public void notificarReceitaAdicionada(String nomeReceita,  BigDecimal valor, Integer userId){
        notificationService.criarNotificacao(
            " ✅ Receita adicionada!",
            "Você adicionou uma receita de R$ \" + valor + \" (\" + nomereceita + \")",
            "income_sucess",
            userId
        );
    }
    
    
    public void verificarLimiteCategoria(Category categoria, Integer userId){
        if (categoria.getLimitAmount() == null){
            return;
        }

        BigDecimal totalGasto = expenseRepository.sumByUserIdAndCategoryIdCurrentMonth(userId, categoria.getCategoryid());
        if (totalGasto == null) {
            totalGasto = BigDecimal.ZERO;
        }

        BigDecimal limite = categoria.getLimitAmount();

        BigDecimal percentualGasto = totalGasto.divide(limite, 2, java.math.RoundingMode.HALF_UP)
                                              .multiply(new BigDecimal("100"));

        if (percentualGasto.compareTo(new BigDecimal("80")) >= 0 && 
            percentualGasto.compareTo(new BigDecimal("100")) < 0) {
            
            notificationService.criarNotificacao(
                "⚠️ Limite em Alerta",
                "Você atingiu 80% do limite de " + categoria.getCategoryname() + 
                " (R$ " + totalGasto + " de R$ " + limite + ")",
                "limit_warning_80",
                userId
            );
        }

        else if (percentualGasto.compareTo(new BigDecimal("100")) >= 0) {
            notificationService.criarNotificacao(
                "🚨 Limite Excedido",
                "Você excedeu o limite de " + categoria.getCategoryname() + 
                " (R$ " + totalGasto + " de R$ " + limite + ")",
                "limit_exceeded_100",
                userId
            );
        }
    }
}
