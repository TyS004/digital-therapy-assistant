package com.digitaltherapyassistant.service;

import java.util.List;
import java.util.UUID;

import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;

public interface SessionService {
    List<SessionModuleDto> getSessionLibrary(UUID userId);
    SessionDetail getSessionDetails (UUID sessionId);
    ActiveSession startSession(UUID userId, UUID sessionId);
    ChatResponse chat(UUID sessionId, String message);
    SessionSummary endSession(UUID sessionId, String reason);
    List<SessionHistoryEntry> getSessionHistory(UUID userId);
}