package com.hardik.expensetracker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String category;

    public ExpenseResponse(Long id, String description, BigDecimal amount, LocalDate date, String category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
}