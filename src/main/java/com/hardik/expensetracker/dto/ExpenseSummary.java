package com.hardik.expensetracker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExpenseSummary {
    private BigDecimal totalAmount;
    private List<CategorySummary> categories;

    public ExpenseSummary(BigDecimal totalAmount, List<CategorySummary> categories) {
        this.totalAmount = totalAmount;
        this.categories = categories;
    }

    @Data
    public static class CategorySummary {
        private String category;
        private BigDecimal total;

        public CategorySummary(String category, BigDecimal total) {
            this.category = category;
            this.total = total;
        }
    }
}