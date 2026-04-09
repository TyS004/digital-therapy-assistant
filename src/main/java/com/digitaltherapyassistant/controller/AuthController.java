package com.digitaltherapyassistant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.request.auth.RefreshRequest;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;
import com.digitaltherapyassistant.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication — login to obtain a JWT token for API access")
public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService){
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Create a new user and save to the database")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "basic user information: name, email, and password")
            @RequestBody RegisterRequest request)
    { 
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Log in a user", description = "Log into an existing user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "basic user login information: email and password")
            @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }   

    @Operation(summary = "Refresh Access Token", description = "Refresh user access token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Parameter(description = "refresh token")
            @RequestBody RefreshRequest request){
        String refreshToken = request.getRefreshToken();
        refreshToken = refreshToken.replaceAll("^\"|\"$", "");
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Logs a user out", description = "fully log user out of the application")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "the access token")
            @RequestBody String accessToken){
        authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
