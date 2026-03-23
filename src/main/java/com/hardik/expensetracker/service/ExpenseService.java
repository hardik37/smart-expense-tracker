package com.hardik.expensetracker.service;

import com.hardik.expensetracker.dto.ExpenseRequest;
import com.hardik.expensetracker.dto.ExpenseResponse;
import com.hardik.expensetracker.dto.ExpenseSummary;
import com.hardik.expensetracker.exception.BadRequestException;
import com.hardik.expensetracker.exception.ResourceNotFoundException;
import com.hardik.expensetracker.model.Category;
import com.hardik.expensetracker.model.Expense;
import com.hardik.expensetracker.model.User;
import com.hardik.expensetracker.repository.CategoryRepository;
import com.hardik.expensetracker.repository.ExpenseRepository;
import com.hardik.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public ExpenseResponse createExpense(String email, ExpenseRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());
                    return categoryRepository.save(newCategory);
                });

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(category);
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    public List<ExpenseResponse> getExpenses(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return expenseRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Expense does not belong to user");
        }

        return toResponse(expense);
    }

    @Transactional
    public ExpenseResponse updateExpense(String email, Long id, ExpenseRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Expense does not belong to user");
        }

        Category category = categoryRepository.findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());
                    return categoryRepository.save(newCategory);
                });

        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(category);

        Expense updated = expenseRepository.save(expense);
        return toResponse(updated);
    }

    @Transactional
    public void deleteExpense(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Expense does not belong to user");
        }

        expenseRepository.delete(expense);
    }

    public ExpenseSummary getSummary(String email, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal total = expenseRepository.getTotalAmountByUserIdAndDateBetween(
                user.getId(), startDate, endDate);

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(
                user.getId(), startDate, endDate);

        List<ExpenseSummary.CategorySummary> categorySummaries = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ))
                .entrySet().stream()
                .map(entry -> new ExpenseSummary.CategorySummary(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ExpenseSummary(total != null ? total : BigDecimal.ZERO, categorySummaries);
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory().getName()
        );
    }
}