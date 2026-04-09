package com.digitaltherapyassistant.cli.api.auth;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;

@Component
public interface AuthAPIClient {
    void register(RegisterRequest request);
    void login(LoginRequest request);
    void refreshToken(String refreshToken);
    void logout(String accessToken);
}
