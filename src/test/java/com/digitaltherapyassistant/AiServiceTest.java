package com.digitaltherapyassistant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;

import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.repository.CbtSessionRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import com.digitaltherapyassistant.service.AiService;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.RagContextBuilder;

@ExtendWith(MockitoExtension.class)
public class AiServiceTest {
    @Mock private CrisisServiceInterface crisisService;
    @Mock private RagContextBuilder ragContextBuilder;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private ChatClient chatClient;
    @Mock private CbtSessionRepository cbtSessionRepository;
    @Mock private UserSessionRepository userSessionRepository;

    @Mock private ChatResponse modelChatResponse;

    private AiService aiService;

    @BeforeEach
    void setup() {
        aiService = new AiService(
                crisisService,
                ragContextBuilder,
                chatClient,
                cbtSessionRepository,
                userSessionRepository);
    }

    @Test
    public void testGenerateResponse() {
        UUID cbtSessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID userSessionId = UUID.randomUUID();

        CbtSession cbtSession = new CbtSession();
        cbtSession.setId(cbtSessionId);
        User user = new User();
        user.setId(userId);
        UserSession userSession = new UserSession();
        userSession.setId(userSessionId);
        userSession.setUser(user);
        userSession.setCbtSession(cbtSession);

        when(cbtSessionRepository.findById(cbtSessionId)).thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession)).thenReturn(Optional.of(userSession));
        when(ragContextBuilder.buildContext(userId, userSessionId, "hello")).thenReturn("ctx");
        when(chatClient.prompt().system("You are a CBT therapy assistant. Be empathetic, concise, and practical.")
                .user("Context:\nctx\n\nUser message:\nhello")
                .call()
                .chatResponse()).thenReturn(modelChatResponse);

        ChatResponse response = aiService.generateResponse(cbtSessionId, "hello");
        assertEquals(modelChatResponse, response);
        verify(ragContextBuilder).buildContext(userId, userSessionId, "hello");
    }

    @Test
    public void testSummarizeSession() {
        UUID cbtSessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID userSessionId = UUID.randomUUID();

        CbtSession cbtSession = new CbtSession();
        cbtSession.setId(cbtSessionId);
        User user = new User();
        user.setId(userId);
        UserSession userSession = new UserSession();
        userSession.setId(userSessionId);
        userSession.setUser(user);
        userSession.setCbtSession(cbtSession);

        when(cbtSessionRepository.findById(cbtSessionId)).thenReturn(Optional.of(cbtSession));
        when(userSessionRepository.findByCbtSession(cbtSession)).thenReturn(Optional.of(userSession));
        when(ragContextBuilder.buildContext(userId, userSessionId, "Summarize this CBT session")).thenReturn("session ctx");
        when(chatClient.prompt().system("You summarize CBT sessions clearly and safely.")
                .user("Create a brief summary and actionable next steps from:\nsession ctx")
                .call()
                .content()).thenReturn("Session summary text");

        SessionSummary summary = aiService.summarizeSession(cbtSessionId);
        assertNotNull(summary);
        assertEquals(cbtSessionId, summary.getSessionId());
        assertEquals("Session summary text", summary.getMessage());
    }
}
