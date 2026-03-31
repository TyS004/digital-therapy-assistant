package com.digitaltherapyassistant.service.interfaces;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.model.DiaryInsightsDto;
import com.digitaltherapyassistant.model.DistortionSuggestionDto;
import com.digitaltherapyassistant.model.SessionSummaryDto;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface AiServiceInterface {
    ChatResponse generateResponse(UUID sessionId, String userMessage) ;

    List<DistortionSuggestionDto> analyzeThought(String automaticThought) ;

    List<String> generateReframingPrompts(String thought, List<String> distortionIds) ;

    CrisisDetectionResponse detectCrisis(String text) ;

    DiaryInsightsDto generateInsights(UUID userId) ;

    SessionSummaryDto summarizeSession(UUID sessionId) ;

}

