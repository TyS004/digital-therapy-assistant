package com.digitaltherapyassistant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.digitaltherapyassistant.service.SessionServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTest {
    @Mock private CbtSessionRepository cbtSessionRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserSessionRepository userSessionRepository;
    @Mock private ChatMessageRepository chatMessageRepository;

    @InjectMocks private SessionServiceImpl sessionService;

    @Test
    public void testGetSessionLibrary(){
        List<CbtSession> sessions = new ArrayList<>();
        List<SessionModule> modules = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            CbtSession session = new CbtSession();
            SessionModule module = new SessionModule();

            session.setModule(module);
            sessions.add(session);
            modules.add(i, module);
        }

        //200 OK
        when(cbtSessionRepository.findAll())
        .thenReturn(sessions);

        assertDoesNotThrow(() -> sessionService.getSessionLibrary(null));
        List<SessionModuleDto> response = sessionService.getSessionLibrary(null);

        for(int i = 0; i < 5; ++i){
            assertEquals("Set Module", response.get(i).getMessage());
            assertEquals(modules.get(i), response.get(i).getModule());
        }
    }

    @Test
    public void testGetSessionDetails(){
        UUID sessionId = UUID.randomUUID();
        CbtSession session = new CbtSession();
        session.setId(sessionId);

        //200 OK
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(session));

        assertDoesNotThrow(() -> sessionService.getSessionDetails(sessionId));

        SessionDetail response = sessionService.getSessionDetails(sessionId);
        assertEquals("Retireved Session Details", response.getMessage());
        assertEquals(session, response.getSession());

        //400 Session Not Found
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
        UserSession userSession = new UserSession();
    
        userSession.setCbtSession(session);
    
        //200 OK
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(session));
        when(userSessionRepository.findByCbtSession(session))
        .thenReturn(Optional.empty());
    
        assertDoesNotThrow(() -> sessionService.startSession(userId, sessionId));
    
        ActiveSession response = sessionService.startSession(userId, sessionId);
        assertEquals("Session Started", response.getMessage());
        assertEquals(Status.IN_PROGRESS, response.getSession().getStatus());
    
        //400 Session Already Started
        when(userSessionRepository.findByCbtSession(session))
        .thenReturn(Optional.of(userSession));
    
        assertThrows(DigitalTherapyException.class,
            () -> sessionService.startSession(userId, sessionId));
    
        //400 User Not Found
        when(userRepository.findById(userId))
        .thenReturn(Optional.empty());
    
        assertThrows(DigitalTherapyException.class,
            () -> sessionService.startSession(userId, sessionId));
    
        //400 Session Not Found
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

        //200 OK
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession))
        .thenReturn(Optional.of(userSession));
        when(chatMessageRepository.findAllByUserSession(userSession))
        .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> sessionService.chat(sessionId, message));

        ChatResponse response = sessionService.chat(sessionId, message);
        assertEquals("Message Set", response.getMessage());
        assertEquals(message, response.getChatMessage().getContent());
        assertEquals(Role.USER, response.getChatMessage().getRole());

        //400 CBT Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.chat(sessionId, message));

        //400 User Session Not Found
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

        //200 OK
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession))
        .thenReturn(Optional.of(userSession));

        assertDoesNotThrow(() -> sessionService.endSession(sessionId, reason));

        SessionSummary response = sessionService.endSession(sessionId, reason);
        assertEquals("Session Ended", response.getMessage());
        assertEquals(cbtSession, response.getSession());
        assertEquals(reason, response.getReason());

        //400 CBT Session Not Found
        when(cbtSessionRepository.findById(sessionId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.endSession(sessionId, reason));

        //400 User Session Not Found
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
            userSessions.add(userSession);
        }

        //200 OK
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(userSessionRepository.findAllByUser(user))
        .thenReturn(Optional.of(userSessions));

        assertDoesNotThrow(() -> sessionService.getSessionHistory(userId));

        List<SessionHistoryEntry> response = sessionService.getSessionHistory(userId);
        assertEquals(3, response.size());
        for(int i = 0; i < 3; ++i){
            assertEquals("Set Session " + userSessions.get(i).getId(), response.get(i).getMessage());
            assertEquals(userSessions.get(i), response.get(i).getUserSession());
        }

        //400 User Not Found
        when(userRepository.findById(userId))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.getSessionHistory(userId));

        //400 No Sessions Started
        when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
        when(userSessionRepository.findAllByUser(user))
        .thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class,
            () -> sessionService.getSessionHistory(userId));
    }
}
