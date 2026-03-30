package com.digitaltherapyassistant.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.cli.commands.cbt.CBTSessionsMenuCommand;
import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.ChatMessage;
import com.digitaltherapyassistant.entity.Role;
import com.digitaltherapyassistant.entity.SessionModule;
import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.repository.CbtSessionRepository;
import com.digitaltherapyassistant.repository.ChatMessageRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;

@Service
public class SessionServiceImpl implements SessionService{
    private final CbtSessionRepository cbtSessionRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    public SessionServiceImpl(CbtSessionRepository cbtSessionRepository,
                                UserRepository userRepository,
                                UserSessionRepository userSessionRepository,
                                ChatMessageRepository chatMessageRepository
    ){
        this.cbtSessionRepository = cbtSessionRepository;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<SessionModuleDto> getSessionLibrary(UUID userId){
        List<SessionModuleDto> response = new ArrayList<>();
        List<CbtSession> sessions = cbtSessionRepository.findAll();

        for(CbtSession session : sessions){
            SessionModule sessionModule = session.getModule();
            SessionModuleDto moduleDto = new SessionModuleDto();
            moduleDto.setModule(sessionModule);
            moduleDto.setMessage("Set Module");
            response.add(moduleDto);
        }
        return response;
    }

    public SessionDetail getSessionDetails (UUID sessionId){
        SessionDetail response = new SessionDetail();
        CbtSession session = cbtSessionRepository.findById(sessionId).orElse(null);
        if(session == null){
            response.setMessage("Session Not Found");
            return response;
        }
        response.setSession(session);
        response.setMessage(null);
        return response;
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
        ChatResponse response = new ChatResponse();
        ChatMessage chatMessage = new ChatMessage();
        //CbtSession cbtSession = cbtSessionRepository.findById(sessionId).orElse(null);

        chatMessage.setContent(message);
        chatMessage.setRole(Role.USER);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setUserSession(null);
        chatMessageRepository.save(chatMessage);

        response.setChatMessage(chatMessage);
        response.setMessage("Message Set");
        return response;
    }

    public SessionSummary endSession(UUID sessionId, String reason){
        return new SessionSummary();
    }
    
    public List<SessionHistoryEntry> getSessionHistory(UUID userId){
        return List.of(new SessionHistoryEntry());
    }
}