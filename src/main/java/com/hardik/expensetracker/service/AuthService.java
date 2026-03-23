package com.hardik.expensetracker.service;

import com.hardik.expensetracker.dto.AuthResponse;
import com.hardik.expensetracker.dto.LoginRequest;
import com.hardik.expensetracker.dto.RegisterRequest;
import com.hardik.expensetracker.exception.BadRequestException;
import com.hardik.expensetracker.exception.ResourceNotFoundException;
import com.hardik.expensetracker.model.User;
import com.hardik.expensetracker.repository.UserRepository;
import com.hardik.expensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, "User registered successfully");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, "Login successful");
    }
}