package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.model.Distortion;
import com.digitaltherapyassistant.repository.CbtSessionRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import com.digitaltherapyassistant.service.interfaces.AiServiceInterface;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.RagContextBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiService implements AiServiceInterface {

    private final CrisisServiceInterface crisisService ;
    private final RagContextBuilder ragContextBuilder ;
    private final ChatClient chatClient;
    private final CbtSessionRepository cbtSessionRepository;
    private final UserSessionRepository userSessionRepository;


    public AiService(
            CrisisServiceInterface crisisService,
            RagContextBuilder ragContextBuilder,
            ChatClient chatClient,
            CbtSessionRepository cbtSessionRepository,
            UserSessionRepository userSessionRepository) {
        this.crisisService = crisisService ;
        this.ragContextBuilder = ragContextBuilder ;
        this.chatClient = chatClient;
        this.cbtSessionRepository = cbtSessionRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public ChatResponse generateResponse(UUID sessionId, String userMessage) {
        UserSession userSession = getUserSessionByCbtSessionId(sessionId);
        String context = this.ragContextBuilder.buildContext(
                userSession.getUser().getId(),
                userSession.getId(),
                userMessage);
        return chatClient.prompt()
                .system("You are a CBT therapy assistant. Be empathetic, concise, and practical.")
                .user("Context:\n" + context + "\n\nUser message:\n" + userMessage)
                .call()
                .chatResponse();
    }

    @Override
    public List<Distortion> analyzeThought(String automaticThought) {
        List<Distortion> suggestions = new ArrayList<>() ;

        return suggestions;
    }

    @Override
    public List<String> generateReframingPrompts(String thought, List<String> distortionIds) {

        return null;
    }

    @Override
    public CrisisDetectionResponse detectCrisis(String text) {

        return this.crisisService.detectCrisis(text) ;
    }

    @Override
    public DiaryInsights generateInsights(UUID userId) {
        return null;
    }

    @Override
    public SessionSummary summarizeSession(UUID sessionId) {
        UserSession userSession = getUserSessionByCbtSessionId(sessionId);
        String context = this.ragContextBuilder.buildContext(
                userSession.getUser().getId(),
                userSession.getId(),
                "Summarize this CBT session");
        String summaryText = chatClient.prompt()
                .system("You summarize CBT sessions clearly and safely.")
                .user("Create a brief summary and actionable next steps from:\n" + context)
                .call()
                .content();

        SessionSummary summary = new SessionSummary();
        summary.setSessionId(sessionId);
        summary.setMessage(summaryText);
        return summary;
    }

    private UserSession getUserSessionByCbtSessionId(UUID sessionId) {
        CbtSession cbtSession = cbtSessionRepository.findById(sessionId)
                .orElseThrow(() -> new DigitalTherapyException("CBT Session Not Found"));

        return userSessionRepository.findByCbtSession(cbtSession)
                .orElseThrow(() -> new DigitalTherapyException("User Session Not Found"));
    }
}
