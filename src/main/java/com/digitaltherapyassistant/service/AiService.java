package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.model.DiaryInsightsDto;
import com.digitaltherapyassistant.model.DistortionSuggestionDto;
import com.digitaltherapyassistant.model.SessionSummaryDto;
import com.digitaltherapyassistant.service.interfaces.AiServiceInterface;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiService implements AiServiceInterface {

    private CrisisServiceInterface crisisService ;

    @Override
    public ChatResponse generateResponse(UUID sessionId, String userMessage) {
        return null;
    }

    @Override
    public List<DistortionSuggestionDto> analyzeThought(String automaticThought) {
        List<DistortionSuggestionDto> suggestions = new ArrayList<>() ;

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
    public DiaryInsightsDto generateInsights(UUID userId) {
        return new DiaryInsightsDto();
    }

    @Override
    public SessionSummaryDto summarizeSession(UUID sessionId) {
        return new SessionSummaryDto();
    }
}
