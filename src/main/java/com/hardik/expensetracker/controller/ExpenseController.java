package com.hardik.expensetracker.controller;

import com.hardik.expensetracker.dto.ExpenseRequest;
import com.hardik.expensetracker.dto.ExpenseResponse;
import com.hardik.expensetracker.dto.ExpenseSummary;
import com.hardik.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            Authentication authentication,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.createExpense(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(Authentication authentication) {
        return ResponseEntity.ok(expenseService.getExpenses(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            Authentication authentication,
            @PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            Authentication authentication,
            @PathVariable Long id) {
        expenseService.deleteExpense(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummary> getSummary(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(expenseService.getSummary(authentication.getName(), startDate, endDate));
    }
}