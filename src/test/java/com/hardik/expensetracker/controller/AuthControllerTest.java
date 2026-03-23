package com.hardik.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardik.expensetracker.dto.LoginRequest;
import com.hardik.expensetracker.dto.RegisterRequest;
import com.hardik.expensetracker.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(new com.hardik.expensetracker.dto.AuthResponse("jwtToken", "User registered successfully"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new com.hardik.expensetracker.dto.AuthResponse("jwtToken", "Login successful"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"));
    }
}