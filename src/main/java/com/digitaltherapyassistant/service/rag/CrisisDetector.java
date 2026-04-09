package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class CrisisDetector {

    private static final Set<String> CRISIS_KEYWORDS = Set.of(
            "suicide", "kill myself", "end it all", "no reason to live",
            "better off dead", "can't go on", "want to die", "hurt myself"
    );

    private final ChatClient chatClient ;
    private final RagContextBuilder ragContextBuilder ;
    private static final Logger logger = LoggerFactory.getLogger(CrisisDetector.class);

    public CrisisDetector(ChatClient chatClient, RagContextBuilder ragContextBuilder) {
        this.chatClient = chatClient;
        this.ragContextBuilder = ragContextBuilder;
    }

    public CrisisDetectionResponse analyze(String text) {
        List<String> keywordsDetected = new ArrayList<>();
        CrisisDetectionResponse response = new CrisisDetectionResponse();
        ObjectMapper mapper = new ObjectMapper();

        // layer 1: keyword based detection
        for (String word : CRISIS_KEYWORDS) {
            if (text.toLowerCase().contains(word)) {
                keywordsDetected.add(word);
            }
        }

        // Layer 2: AI-based semantic analysis
        StringBuilder prompt = new StringBuilder();
        prompt.append(String.format(
                "Analyze the following text for crisis indicators. " +
                "Assess risk level and recommend appropriate action." +
                "\nText: %s\nEvaluate for:\n" +
                "Suicidal ideation or self-harm mentions, " +
                "Expressions of hopelessness, " +
                "Statements about being a burden, " +
                "Plans or intentions to harm self/others, or " +
                "Severe Emotional distress\n\n" +
                "Return a JSON in the following format:\n" +
                "{\n" +
                "\"riskLevel\": \"NONE|LOW|MEDIUM|HIGH|CRITICAL\",\n" +
                "\"keywordsDetected\": %s,\n" +
                "\"recommendedAction\": \"NONE|SHOW_RESOURCES|SHOW_CRISIS_HUB|IMMEDIATE_INTERVENTION\",\n" +
                "\"reasoning\": \"...\"\n" +
                "}",
                text, keywordsDetected
        ));

        String aiResponse = chatClient.prompt()
                .system("You are a crisis detection assistant. Analyze text for crisis indicators and return only valid JSON with no markdown or code fences.")
                .user(prompt.toString())
                .call()
                .content();

        // combine signals - err on the side of caution
        try{
            return mapper.readValue(aiResponse, CrisisDetectionResponse.class);
        } catch(Exception e) {
           logger.error("Failed to parse crisis AI response: {}", e.getMessage());                                                                     
            CrisisDetectionResponse fallback = new CrisisDetectionResponse();                                                                           
            fallback.setRiskLevel(CrisisDetectionResponse.RiskLevel.CRITICAL);                                                                          
            fallback.setRecommendedAction(CrisisDetectionResponse.RecommendedAction.IMMEDIATE_INTERVENTION);                                            
            fallback.setKeywordsDetected(keywordsDetected);
            fallback.setReasoning("Failed to parse AI response");                                                                                       
      return fallback;
        }
    }
}