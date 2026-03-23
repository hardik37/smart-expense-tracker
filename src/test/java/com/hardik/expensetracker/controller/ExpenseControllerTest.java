package com.hardik.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardik.expensetracker.dto.ExpenseRequest;
import com.hardik.expensetracker.dto.ExpenseResponse;
import com.hardik.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "john@example.com")
    void testGetExpenses_Success() throws Exception {
        ExpenseResponse response = new ExpenseResponse(1L, "Lunch", new BigDecimal("20.00"), LocalDate.now(), "Food");
        
        when(expenseService.getExpenses(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Lunch"))
                .andExpect(jsonPath("$[0].amount").value(20.00));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    void testCreateExpense_Success() throws Exception {
        ExpenseRequest request = new ExpenseRequest();
        request.setDescription("Lunch");
        request.setAmount(new BigDecimal("20.00"));
        request.setDate(LocalDate.now());
        request.setCategory("Food");

        ExpenseResponse response = new ExpenseResponse(1L, "Lunch", new BigDecimal("20.00"), LocalDate.now(), "Food");

        when(expenseService.createExpense(anyString(), any(ExpenseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/expenses")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Lunch"));
    }
}