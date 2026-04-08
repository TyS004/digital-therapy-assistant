package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.model.Distortion;
import com.digitaltherapyassistant.service.interfaces.AiServiceInterface;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.RagContextBuilder;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiService implements AiServiceInterface {

    private final CrisisServiceInterface crisisService ;
    private final RagContextBuilder ragContextBuilder ;


    public AiService(CrisisServiceInterface crisisService, RagContextBuilder ragContextBuilder) {
        this.crisisService = crisisService ;
        this.ragContextBuilder = ragContextBuilder ;
    }

    @Override
    public ChatResponse generateResponse(UUID sessionId, String userMessage) {
        return null;
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
        return new SessionSummary();
    }
}
