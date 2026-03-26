package com.digitaltherapyassistant.service;

import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.dto.request.LoginRequest;
import com.digitaltherapyassistant.dto.request.RegisterRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;

@Service
public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String accessToken);
}