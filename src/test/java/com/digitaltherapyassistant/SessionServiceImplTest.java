package com.digitaltherapyassistant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.entity.CbtSession;
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
import com.digitaltherapyassistant.service.SessionServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTest {
    @Mock private CbtSessionRepository cbtSessionRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserSessionRepository userSessionRepository;
    @Mock private ChatMessageRepository chatMessageRepository;
    @Mock private SessionModuleRepository sessionModuleRepository;
    @Mock private AiServiceInterface aiService;

    @InjectMocks private SessionServiceImpl sessionService;

    @Test
    public void testGetSessionLibrary(){
        UUID sessionId = UUID.randomUUID();
        List<CbtSession> cbtSessions = new ArrayList<>();
        CbtSession session = new CbtSession();
        session.setId(sessionId);
        session.setTitle("Test Session");
        session.setDescription("Test Description");
        session.setDurationMinutes(30);
        session.setObjectives(new ArrayList<>());
        session.setModalities(new ArrayList<>());
        session.setOrderIndex(1);
        cbtSessions.add(session);

        List<SessionModule> modules = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            SessionModule module = new SessionModule();
            module.setId(UUID.randomUUID().toString());
            module.setCbtSessions(cbtSessions);
            modules.add(module);
        }

        when(sessionModuleRepository.findAll())
        .thenReturn(modules);

        assertDoesNotThrow(() -> sessionService.getSessionLibrary(null));
        List<SessionModuleDto> response = sessionService.getSessionLibrary(null);

        assertEquals(5, response.size());
        for(int i = 0; i < 5; ++i){
            assertEquals(modules.get(i).getId(), response.get(i).getId());
            //assertEquals(modules.get(i).getCbtSessions(), response.get(i).getSessions());
            assertEquals("Retreived Session Data for Module", response.get(i).getMessage());
        }
    }

    @Test
    public void testGetSessionDetails(){
        UUID sessionId = UUID.randomUUID();
        CbtSession session = new CbtSession();
        session.setId(sessionId);
        session.setTitle("Test Session");
        session.setDescription("Test Description");
        session.setDurationMinutes(30);
        session.setObjectives(new ArrayList<>());
        session.setModalities(new ArrayList<>());
        session.setOrderIndex(1);

        // 200 OK
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(session));

        assertDoesNotThrow(() -> sessionService.getSessionDetails(sessionId));

        SessionDetail response = sessionService.getSessionDetails(sessionId);
        assertEquals("Retireved Session Details", response.getMessage());
        assertEquals(sessionId, response.getId());
        assertEquals("Test Session", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        assertEquals(30, response.getDurationMinutes());
        assertEquals(1, response.getOrderIndex());

        // 400 Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.getSessionDetails(sessionId));
    }

    @Test
    public void testStartSession(){
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
    
        User user = new User();
        CbtSession session = new CbtSession();
        session.setId(sessionId);
        session.setTitle("Test Session");
        UserSession userSession = new UserSession();
        userSession.setCbtSession(session);
    
        // 200 OK
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(session));
        when(userSessionRepository.findByCbtSession(session))
        .thenReturn(Optional.empty());
    
        assertDoesNotThrow(() -> sessionService.startSession(userId, sessionId));
    
        ActiveSession response = sessionService.startSession(userId, sessionId);
        assertEquals("Session Started", response.getMessage());
        assertEquals(sessionId, response.getSessionId());
        assertEquals("Test Session", response.getTitle());
        assertEquals(Status.IN_PROGRESS, response.getStatus());
    
        // 400 Session Already Started
        when(userSessionRepository.findByCbtSession(session))
        .thenReturn(Optional.of(userSession));
    
        assertThrows(DigitalTherapyException.class,
            () -> sessionService.startSession(userId, sessionId));
    
        // 400 User Not Found
        when(userRepository.findById(userId))
        .thenReturn(Optional.empty());
    
        assertThrows(DigitalTherapyException.class,
            () -> sessionService.startSession(userId, sessionId));
    
        // 400 Session Not Found
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.empty());
    
        assertThrows(DigitalTherapyException.class,
            () -> sessionService.startSession(userId, sessionId));
    }

    @Test
    public void testChat(){
        UUID sessionId = UUID.randomUUID();
        String message = "Hello";

        CbtSession cbtSession = new CbtSession();
        UserSession userSession = new UserSession();
        userSession.setChatMessages(new ArrayList<>());

        // 200 OK
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession))
        .thenReturn(Optional.of(userSession));
        when(chatMessageRepository.findAllByUserSession(userSession))
        .thenReturn(new ArrayList<>());

        ChatResponse response = sessionService.chat(sessionId, message);
        assertEquals("Message Set", response.getMessage());
        assertEquals(sessionId, response.getSessionId());
        assertNull(response.getAssistantMessage());
        verify(aiService).generateResponse(sessionId, message);

        // 400 CBT Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.chat(sessionId, message));

        // 400 User Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.chat(sessionId, message));
    }

    @Test
    public void testEndSession(){
        UUID sessionId = UUID.randomUUID();
        String reason = "Completed";

        CbtSession cbtSession = new CbtSession();
        UserSession userSession = new UserSession();

        userSession.setMoodBefore(5);
        userSession.setMoodAfter(5);

        // 200 OK
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession))
        .thenReturn(Optional.of(userSession));

        SessionSummary response = sessionService.endSession(sessionId, reason);
        assertEquals("Session Ended", response.getMessage());
        verify(aiService).summarizeSession(sessionId);
        assertEquals(sessionId, response.getSessionId());
        assertEquals(Status.COMPLETED, response.getStatus());
        assertEquals(reason, response.getReason());

        // 400 CBT Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.endSession(sessionId, reason));

        // 400 User Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.endSession(sessionId, reason));
    }

    @Test
    public void testGetSessionHistory(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        List<UserSession> userSessions = new ArrayList<>();
        for(int i = 0; i < 3; ++i){
            UserSession userSession = new UserSession();
            userSession.setId(UUID.randomUUID());
            userSession.setMoodBefore(5);
            userSession.setMoodAfter(5);
            userSession.setStatus(Status.IN_PROGRESS);
            userSession.setChatMessages(new ArrayList<>());
            userSession.setEndedAt(null);
            userSession.setUser(new User());
            userSessions.add(userSession);
        }

        // 200 OK
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(userSessionRepository.findAllByUser(user))
        .thenReturn(Optional.of(userSessions));

        assertDoesNotThrow(() -> sessionService.getSessionHistory(userId));

        List<SessionHistoryEntry> response = sessionService.getSessionHistory(userId);
        assertEquals(3, response.size());
        for(int i = 0; i < 3; ++i){
            assertEquals(userSessions.get(i).getId(), response.get(i).getSessionId());
            assertEquals(userSessions.get(i).getStatus(), response.get(i).getStatus());
            assertEquals(userSessions.get(i).getMoodBefore(), response.get(i).getMoodBefore());
            assertEquals(userSessions.get(i).getMoodAfter(), response.get(i).getMoodAfter());
            assertEquals("Set Session " + userSessions.get(i).getId(), response.get(i).getMessage());
        }

        // 400 User Not Found
        when(userRepository.findById(userId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.getSessionHistory(userId));

        // 400 No Sessions Started
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(userSessionRepository.findAllByUser(user))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.getSessionHistory(userId));
    }
}