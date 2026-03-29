package com.digitaltherapyassistant.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;

@Service
public class SessionServiceImpl implements SessionService{


    public List<SessionModuleDto> getSessionLibrary(UUID userId){
        return List.of(new SessionModuleDto());
    }

    public SessionDetail getSessionDetails (UUID sessionId){
        return new SessionDetail();
    }

    public ActiveSession startSession(UUID userId, UUID sessionId){
        return new ActiveSession();
    }

    public ChatResponse chat(UUID sessionId, String message){
        return new ChatResponse();
    }

    public SessionSummary endSession(UUID sessionId, String reason){
        return new SessionSummary();
    }
    
    public List<SessionHistoryEntry> getSessionHistory(UUID userId){
        return List.of(new SessionHistoryEntry());
    }
}