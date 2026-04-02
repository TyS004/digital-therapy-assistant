package com.digitaltherapyassistant.cli.api.session;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public interface SessionAPIClient {
    public void getSessionLibrary(UUID userId);
    public void startSession(UUID userId, UUID sessionId);
    public void getSessionHistory(UUID userId);
}
