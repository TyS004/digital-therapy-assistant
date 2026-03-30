package com.digitaltherapyassistant.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.repository.CbtSessionRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;

@Service
public class SessionServiceImpl implements SessionService{
    private final CbtSessionRepository cbtSessionRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    public SessionServiceImpl(CbtSessionRepository cbtSessionRepository,
                                UserRepository userRepository,
                                UserSessionRepository userSessionRepository
    ){
        this.cbtSessionRepository = cbtSessionRepository;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    public List<SessionModuleDto> getSessionLibrary(UUID userId){
        return List.of(new SessionModuleDto());
    }

    public SessionDetail getSessionDetails (UUID sessionId){
        return new SessionDetail();
    }

    public ActiveSession startSession(UUID userId, UUID sessionId) {
        ActiveSession activeSession = new ActiveSession();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            activeSession.setMessage("User Not Found");
            return activeSession;
        }

        CbtSession cbtSession = cbtSessionRepository.findById(sessionId).orElse(null);
        if (cbtSession == null) {
            activeSession.setMessage("Session Not Found");
            return activeSession;
        }   

        UserSession userSession = new UserSession();
        userSession.setCbtSession(cbtSession);
        userSession.setUser(user);
        userSession.setStatus(Status.IN_PROGRESS);
        userSession.setStartedAt(LocalDateTime.now());
        userSessionRepository.save(userSession);

        activeSession.setSession(userSession);
        activeSession.setMessage("Session Started");
        return activeSession;
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