package com.digitaltherapyassistant.service.interfaces;

import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.DistortionSuggestion;
import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.model.Distortion;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface AiServiceInterface {
    ChatResponse generateResponse(UUID sessionId, String userMessage) ;

    List<DistortionSuggestion> analyzeThought(String automaticThought) ;

    List<String> generateReframingPrompts(String thought, List<String> distortionIds) ;

    CrisisDetectionResponse detectCrisis(String text) ;

    DiaryInsights generateInsights(UUID userId) ;

    SessionSummary summarizeSession(UUID sessionId) ;

}

