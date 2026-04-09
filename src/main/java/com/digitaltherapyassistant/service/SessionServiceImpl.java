package com.digitaltherapyassistant.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionDto;
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
import com.digitaltherapyassistant.repository.SessionModuleRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import com.digitaltherapyassistant.service.interfaces.AiServiceInterface;

import jakarta.transaction.Transactional;

@Service
public class SessionServiceImpl implements SessionService{
    private final CbtSessionRepository cbtSessionRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SessionModuleRepository sessionModuleRepository;
    private final AiServiceInterface aiService;

    public SessionServiceImpl(CbtSessionRepository cbtSessionRepository,
                                UserRepository userRepository,
                                UserSessionRepository userSessionRepository,
                                ChatMessageRepository chatMessageRepository,
                                SessionModuleRepository sessionModuleRepository,
                                AiServiceInterface aiService
    ){
        this.cbtSessionRepository = cbtSessionRepository;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.sessionModuleRepository = sessionModuleRepository;
        this.aiService = aiService;
    }

    @Transactional
    public List<SessionModuleDto> getSessionLibrary(UUID userId){
        List<SessionModule> modules = sessionModuleRepository.findAll();
        List<SessionModuleDto> response = new ArrayList<>();
        
        for (SessionModule module : modules) {
            SessionModuleDto moduleDto = new SessionModuleDto();
            moduleDto.setId(module.getId());

            List<SessionDto> sessionDtos = new ArrayList<>();
            for (CbtSession session : module.getCbtSessions()) {
                SessionDto sessionDto = new SessionDto();

                sessionDto.setId(session.getId());
                sessionDto.setTitle(session.getTitle());
                sessionDto.setDescription(session.getDescription());
                sessionDto.setDurationMinutes(session.getDurationMinutes());
                sessionDto.setObjectives(session.getObjectives());
                sessionDto.setModalities(session.getModalities());
                sessionDto.setOrderIndex(session.getOrderIndex());
                sessionDtos.add(sessionDto);
            }
            moduleDto.setSessions(sessionDtos);
            moduleDto.setMessage("Retreived Session Data for Module");
            response.add(moduleDto);
        }
        return response;
    }

    public SessionDetail getSessionDetails (UUID sessionId){
        SessionDetail response = new SessionDetail();
        CbtSession session = cbtSessionRepository.findById(sessionId)
            .orElseThrow(() -> new DigitalTherapyException("Session Not Found"));

        response.setId(sessionId);
        response.setDescription(session.getDescription());
        response.setDurationMinutes(session.getDurationMinutes());
        response.setModalities(session.getModalities());
        response.setObjectives(session.getObjectives());
        response.setOrderIndex(session.getOrderIndex());
        response.setTitle(session.getTitle());
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
        userSession.setMoodBefore(5);
        userSession.setMoodAfter(5);
        userSessionRepository.save(userSession);

        response.setMoodBefore(userSession.getMoodBefore());
        response.setStartedAt(userSession.getStartedAt());
        response.setStatus(userSession.getStatus());
        response.setSessionId(sessionId);
        response.setTitle(cbtSession.getTitle());
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

        response.setSessionId(sessionId);
        response.setTimestamp(chatMessage.getTimestamp()); 
        org.springframework.ai.chat.model.ChatResponse aiResponse = aiService.generateResponse(sessionId, message);
        response.setAssistantMessage(aiResponse == null ? null : aiResponse.toString());
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

        response.setStartedAt(userSession.getStartedAt());
        response.setEndedAt(userSession.getEndedAt());
        response.setMoodBefore(userSession.getMoodBefore());
        response.setMoodAfter(userSession.getMoodAfter());
        response.setSessionId(sessionId);
        response.setStatus(userSession.getStatus());
        response.setReason(reason); 
        SessionSummary aiSummary = aiService.summarizeSession(sessionId);
        if (aiSummary != null && aiSummary.getMessage() != null) {
            response.setAiSummary(aiSummary.getAiSummary());
        }
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

            sessionHistory.setStartedAt(session.getStartedAt());
            sessionHistory.setEndedAt(session.getEndedAt());
            sessionHistory.setMoodBefore(session.getMoodBefore());
            sessionHistory.setMoodAfter(session.getMoodAfter());
            sessionHistory.setSessionId(session.getId());
            sessionHistory.setStatus(session.getStatus());
            sessionHistory.setMessage("Set Session " + session.getId());
            response.add(sessionHistory);
        }
        return response;
    }
}