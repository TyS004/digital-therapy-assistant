package com.digitaltherapyassistant.service.interfaces;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface AiServiceInterface {
    ChatResponse generateResponse(UUID sessionId, String userMessage) ;

    List<DistortionSuggestion> analyzeThought(String automaticThought) ;

    List<String> generateReframingPrompts(String thought, List<String> distortionIds) ;

    CrisisDetectionResponse detectCrisis(String text) ;

    DiaryInsightsDto generateInsights(UUID userId) ;

    SessionSummaryDto summarizeSession(UUID sessionId) ;

}

