package com.digitaltherapyassistant.cli.api.auth;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.dto.request.LoginRequest;
import com.digitaltherapyassistant.dto.request.RegisterRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;

@Component
public interface AuthAPIClient {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    AuthResponse logout(String accessToken);
}
