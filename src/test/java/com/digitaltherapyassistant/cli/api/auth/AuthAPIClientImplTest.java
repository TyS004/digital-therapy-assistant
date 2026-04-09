package com.digitaltherapyassistant.cli.api.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;

@ExtendWith(MockitoExtension.class)
public class AuthAPIClientImplTest {
    @Mock private RestTemplate restTemplate;
    @Mock private CLISession session;

    private AuthAPIClientImpl authAPIClient;

    @BeforeEach
    void setup() {
        authAPIClient = new AuthAPIClientImpl(restTemplate, session, "http://localhost:8080");
    }

    @Test
    public void testRegister() {
        UUID userId = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest("u@x.com", "p", "n");
        AuthResponse response = new AuthResponse("access", "refresh", userId, "ok");
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/register"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(response));

        authAPIClient.register(request);
        verify(session).login("http://localhost:8080", userId, "access");
    }

    @Test
    public void testLogin() {
        UUID userId = UUID.randomUUID();
        LoginRequest request = new LoginRequest("u@x.com", "p");
        AuthResponse response = new AuthResponse("access", "refresh", userId, "ok");
        when(session.isLoggedIn()).thenReturn(false);
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/login"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(response));

        authAPIClient.login(request);
        verify(session).login("u@x.com", userId, "access");
    }

    @Test
    public void testRefreshAndLogout() {
        AuthResponse refreshResponse = new AuthResponse("newAccess", "refresh", UUID.randomUUID(), "ok");
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/refresh"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(refreshResponse));
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/logout"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(new AuthResponse()));

        authAPIClient.refreshToken("refresh");
        authAPIClient.logout("access");

        verify(session).setToken("newAccess");
        verify(session).logout();
    }

    @Test
    void registerNullResponseDoesNotLogin() {
        RegisterRequest request = new RegisterRequest("u@x.com", "p", "n");
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/register"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        authAPIClient.register(request);
        verify(session, never()).login(any(), any(), any());
    }

    @Test
    void loginWhenAlreadyLoggedInSkipsRequest() {
        when(session.isLoggedIn()).thenReturn(true);
        authAPIClient.login(new LoginRequest("u@x.com", "p"));
        verify(restTemplate, never()).exchange(
                eq("http://localhost:8080/api/auth/login"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class));
    }

    @Test
    void loginNullResponseDoesNotUpdateSession() {
        when(session.isLoggedIn()).thenReturn(false);
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/login"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        authAPIClient.login(new LoginRequest("u@x.com", "p"));
        verify(session, never()).login(any(), any(), any());
    }

    @Test
    void refreshTokenNullResponseDoesNotSetToken() {
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/auth/refresh"),
                eq(HttpMethod.POST), any(), eq(AuthResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        authAPIClient.refreshToken("refresh");
        verify(session, never()).setToken(any());
    }
}
