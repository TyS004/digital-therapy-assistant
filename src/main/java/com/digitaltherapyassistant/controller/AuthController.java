package com.digitaltherapyassistant.controller;

import java.net.http.HttpResponse;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperationsExtensionsKt;

import com.digitaltherapyassistant.dto.request.LoginRequest;
import com.digitaltherapyassistant.dto.request.RegisterRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;
import com.digitaltherapyassistant.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication — login to obtain a JWT token for API access")
public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService, AuthenticationManager authenticationManager){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    { 
        AuthResponse response = authService.register(request);
        if(response.getMessage().equals("Registered")){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken){
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody String accessToken){
        authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
