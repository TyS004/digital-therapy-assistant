package com.digitaltherapyassistant.service.interfaces;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.model.CrisisDetectionResult;
import com.digitaltherapyassistant.model.DiaryInsights;
import com.digitaltherapyassistant.model.DistortionSuggestion;
import com.digitaltherapyassistant.model.SessionSummary;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface AiServiceInterface {
    ChatResponse generateResponse(UUID sessionId, String userMessage) ;

    List<DistortionSuggestion> analyzeThought(String automaticThought) ;

    List<String> generateReframingPrompts(String thought, List<String> distortionIds) ;

    CrisisDetectionResult detectCrisis(String text) ;

    DiaryInsights generateInsights(UUID userId) ;

    SessionSummary summarizeSession(UUID sessionId) ;

}

