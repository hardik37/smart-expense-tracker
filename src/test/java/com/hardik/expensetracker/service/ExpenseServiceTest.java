package com.hardik.expensetracker.service;

import com.hardik.expensetracker.dto.ExpenseRequest;
import com.hardik.expensetracker.dto.ExpenseResponse;
import com.hardik.expensetracker.exception.ResourceNotFoundException;
import com.hardik.expensetracker.model.Category;
import com.hardik.expensetracker.model.Expense;
import com.hardik.expensetracker.model.User;
import com.hardik.expensetracker.repository.CategoryRepository;
import com.hardik.expensetracker.repository.ExpenseRepository;
import com.hardik.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private User user;
    private Category category;
    private Expense expense;
    private ExpenseRequest expenseRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        category = new Category();
        category.setId(1L);
        category.setName("Food");

        expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Lunch");
        expense.setAmount(new BigDecimal("20.00"));
        expense.setDate(LocalDate.now());
        expense.setCategory(category);
        expense.setUser(user);

        expenseRequest = new ExpenseRequest();
        expenseRequest.setDescription("Lunch");
        expenseRequest.setAmount(new BigDecimal("20.00"));
        expenseRequest.setDate(LocalDate.now());
        expenseRequest.setCategory("Food");
    }

    @Test
    void testGetExpenses_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(expenseRepository.findByUserId(anyLong())).thenReturn(List.of(expense));

        List<ExpenseResponse> responses = expenseService.getExpenses("john@example.com");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Lunch", responses.get(0).getDescription());
    }

    @Test
    void testGetExpenses_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpenses("john@example.com"));
    }

    @Test
    void testCreateExpense_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        ExpenseResponse response = expenseService.createExpense("john@example.com", expenseRequest);

        assertNotNull(response);
        assertEquals("Lunch", response.getDescription());
    }
}