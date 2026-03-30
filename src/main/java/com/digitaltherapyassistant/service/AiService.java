package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.model.CrisisDetectionResult;
import com.digitaltherapyassistant.model.DiaryInsights;
import com.digitaltherapyassistant.model.DistortionSuggestion;
import com.digitaltherapyassistant.model.SessionSummary;
import com.digitaltherapyassistant.service.interfaces.AiServiceInterface;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiService implements AiServiceInterface {
    @Override
    public ChatResponse generateResponse(UUID sessionId, String userMessage) {
        return null;
    }

    @Override
    public List<DistortionSuggestion> analyzeThough(String automaticThought) {
        List<DistortionSuggestion> suggestions = new ArrayList<>() ;

        return suggestions;
    }

    @Override
    public List<String> generateReframingPrompts(String thought, List<String> distortionIds) {

        return null;
    }

    @Override
    public CrisisDetectionResult detectCrisis(String text) {

        return new CrisisDetectionResult();
    }

    @Override
    public DiaryInsights generateInsights(UUID userId) {
        return new DiaryInsights();
    }

    @Override
    public SessionSummary summarizeSession(UUID sessionId) {
        return new SessionSummary();
    }
}
