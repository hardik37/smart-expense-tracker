package com.hardik.expensetracker.service;

import com.hardik.expensetracker.dto.AuthResponse;
import com.hardik.expensetracker.dto.RegisterRequest;
import com.hardik.expensetracker.exception.BadRequestException;
import com.hardik.expensetracker.model.User;
import com.hardik.expensetracker.repository.UserRepository;
import com.hardik.expensetracker.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password123");
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}