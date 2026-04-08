package com.digitaltherapyassistant.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.digitaltherapyassistant.entity.ChatMessage;
import com.digitaltherapyassistant.entity.Role;
import com.digitaltherapyassistant.entity.SessionModule;
import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
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
        CbtSession session = cbtSessionRepository.findById(sessionId)
            .orElseThrow(() -> new DigitalTherapyException("Session Not Found"));

        response.setSession(session);
        response.setMessage("Retireved Session Details");
        return response;
    }

    public ActiveSession startSession(UUID userId, UUID sessionId) {
        ActiveSession response = new ActiveSession();

        User user = userRepository.findById(userId).orElseThrow
            (() -> new DigitalTherapyException("User Not Found"));

        CbtSession cbtSession = cbtSessionRepository.findById(sessionId).orElseThrow
            (() -> new DigitalTherapyException("Session Not Found"));  

        UserSession userSession = userSessionRepository.findByCbtSession(cbtSession).orElse(null);
        if(userSession != null) { throw new DigitalTherapyException("User Session Already Started"); }

        userSession = new UserSession();
        userSession.setCbtSession(cbtSession);
        userSession.setUser(user);
        userSession.setStatus(Status.IN_PROGRESS);
        userSession.setStartedAt(LocalDateTime.now());
        userSession.setChatMessages(new ArrayList<>());
        userSessionRepository.save(userSession);

        response.setSession(userSession);
        response.setMessage("Session Started");
        return response;
    }

    public ChatResponse chat(UUID sessionId, String message){
        ChatResponse response = new ChatResponse();
        ChatMessage chatMessage = new ChatMessage();

        CbtSession cbtSession = cbtSessionRepository.findById(sessionId).orElseThrow
            (() -> new DigitalTherapyException("CBT Session Not Found")); 

        UserSession userSession = userSessionRepository.findByCbtSession(cbtSession).orElseThrow
            (() -> new DigitalTherapyException("User Session Not Found"));

        chatMessage.setContent(message);
        chatMessage.setRole(Role.USER);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setUserSession(userSession);
        chatMessageRepository.save(chatMessage);

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByUserSession(userSession);
        chatMessages.add(chatMessage);

        response.setChatMessage(chatMessage);
        response.setMessage("Message Set");
        return response;
    }

    public SessionSummary endSession(UUID sessionId, String reason){
        SessionSummary response = new SessionSummary();

        CbtSession session = cbtSessionRepository.findById(sessionId).orElseThrow
            (() -> new DigitalTherapyException("CBT Session Not Found"));

        UserSession userSession = userSessionRepository.findByCbtSession(session).orElseThrow
            (() -> new DigitalTherapyException("User Session Not Found"));

        userSession.setEndedAt(LocalDateTime.now());
        userSession.setStatus(Status.COMPLETED);
        userSessionRepository.delete(userSession);

        response.setSession(session);
        response.setReason(reason); 
        response.setMessage("Session Ended");

        return response;
    }
    
    public List<SessionHistoryEntry> getSessionHistory(UUID userId){
        List<SessionHistoryEntry> response = new ArrayList<>();

        User user = userRepository.findById(userId).orElseThrow
            (() -> new DigitalTherapyException("User Not Found"));

        List<UserSession> userSessions = userSessionRepository.findAllByUser(user).orElseThrow
            (() -> new DigitalTherapyException("No Sessions Started"));

        for(UserSession session : userSessions){
            SessionHistoryEntry sessionHistory = new SessionHistoryEntry();
            sessionHistory.setUserSession(session);
            sessionHistory.setMessage("Set Session " + session.getId());
            response.add(sessionHistory);
        }
        return response;
    }
}