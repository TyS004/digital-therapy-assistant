package com.digitaltherapyassistant.cli.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;

@ExtendWith(MockitoExtension.class)
class APIClientTest {

    @Mock private RestTemplate restTemplate;
    @Mock private CLISession session;

    private TestAPIClient client;

    /** Concrete client to exercise protected GET/POST paths. */
    static class TestAPIClient extends APIClient {
        TestAPIClient(RestTemplate restTemplate, CLISession session, String baseUrl) {
            super(restTemplate, session, baseUrl);
        }
    }

    @BeforeEach
    void setUp() {
        client = new TestAPIClient(restTemplate, session, "http://localhost:8080");
    }

    @Test
    void postSuccessAndExceptionReturnsNull() {
        when(session.getToken()).thenReturn(null);
        when(restTemplate.exchange(
                eq("http://localhost:8080/x"),
                eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("body"))
                .thenThrow(new RestClientException("boom"));

        assertEquals("body", client.POST("http://localhost:8080/x", "req", String.class));
        assertNull(client.POST("http://localhost:8080/x", "req", String.class));
    }

    @Test
    void getWithClassSuccessAndException() {
        when(session.getToken()).thenReturn("tok");
        when(restTemplate.exchange(
                eq("http://localhost:8080/y"),
                eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("ok"))
                .thenThrow(new RestClientException("fail"));

        assertEquals("ok", client.GET("http://localhost:8080/y", String.class));
        assertNull(client.GET("http://localhost:8080/y", String.class));
        //verify(session).getToken();
    }

    @Test
    void getWithParameterizedTypeSuccessAndException() {
        when(session.getToken()).thenReturn(null);
        when(restTemplate.exchange(
                eq("http://localhost:8080/z"),
                eq(HttpMethod.GET), any(),
                org.mockito.ArgumentMatchers.<ParameterizedTypeReference<String>>any()))
                .thenReturn(ResponseEntity.ok("p"))
                .thenThrow(new RestClientException("err"));

        assertEquals("p", client.GET("http://localhost:8080/z",
                new ParameterizedTypeReference<String>() {}));
        assertNull(client.GET("http://localhost:8080/z",
                new ParameterizedTypeReference<String>() {}));
    }
}
