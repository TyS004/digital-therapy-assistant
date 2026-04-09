package com.digitaltherapyassistant.cli.api.session;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.SessionDto;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.service.SessionService;

@ExtendWith(MockitoExtension.class)
public class SessionAPIClientImplTest {
    @Mock private RestTemplate restTemplate;
    @Mock private CLISession session;
    @Mock private SessionService sessionService;

    private SessionAPIClientImpl sessionAPIClient;

    @BeforeEach
    void setup() {
        sessionAPIClient = new SessionAPIClientImpl(restTemplate, session, "http://localhost:8080", sessionService);
    }

    @Test
    public void testGetSessionLibrary() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/sessions?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<SessionModuleDto>>>any()))
                .thenReturn(ResponseEntity.ok(List.of()));

        sessionAPIClient.getSessionLibrary(userId);
        verify(restTemplate).exchange(
                eq("http://localhost:8080/api/sessions?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<SessionModuleDto>>>any());
    }

    @Test
    public void testStartSession() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/sessions/" + sessionId + "/start"),
                eq(HttpMethod.POST), any(), eq(ActiveSession.class)))
                .thenReturn(ResponseEntity.ok(new ActiveSession()));

        sessionAPIClient.startSession(userId, sessionId);
        verify(restTemplate).exchange(
                eq("http://localhost:8080/api/sessions/" + sessionId + "/start"),
                eq(HttpMethod.POST), any(), eq(ActiveSession.class));
    }

    @Test
    public void testGetSessionHistory() {
        UUID userId = UUID.randomUUID();
        when(sessionService.getSessionHistory(userId)).thenReturn(List.of(new SessionHistoryEntry()));

        sessionAPIClient.getSessionHistory(userId);
        verify(sessionService).getSessionHistory(userId);
    }

    @Test
    void getSessionLibraryNullResponse() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/sessions?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<SessionModuleDto>>>any()))
                .thenReturn(ResponseEntity.ok(null));

        sessionAPIClient.getSessionLibrary(userId);
    }

    @Test
    void getSessionLibraryWithModulesPrints() {
        UUID userId = UUID.randomUUID();
        UUID sid = UUID.randomUUID();
        SessionDto dto = new SessionDto();
        dto.setId(sid);
        SessionModuleDto mod = new SessionModuleDto();
        mod.setId("mod-1");
        mod.setSessions(List.of(dto));
        mod.setMessage("m");
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/sessions?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<SessionModuleDto>>>any()))
                .thenReturn(ResponseEntity.ok(List.of(mod)));

        sessionAPIClient.getSessionLibrary(userId);
    }

    @Test
    void startSessionNullResponse() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/sessions/" + sessionId + "/start"),
                eq(HttpMethod.POST), any(), eq(ActiveSession.class)))
                .thenReturn(ResponseEntity.ok(null));

        sessionAPIClient.startSession(userId, sessionId);
    }

    @Test
    void getSessionHistoryNullPrintsMessage() {
        UUID userId = UUID.randomUUID();
        when(sessionService.getSessionHistory(userId)).thenReturn(null);

        sessionAPIClient.getSessionHistory(userId);
    }
}
