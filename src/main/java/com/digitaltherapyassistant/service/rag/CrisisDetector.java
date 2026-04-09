package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse.RiskLevel;
import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse.RecommendedAction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    );

    private final ChatClient chatClient;
    private final RagContextBuilder ragContextBuilder;
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
        List<String> keywordsDetected = new ArrayList<>();
        CrisisDetectionResponse response = new CrisisDetectionResponse();

        // Layer 1: Keyword-based detection (fast)
        for (String word : CRISIS_KEYWORDS) {
            if (text.toLowerCase().contains(word)) {
                keywordsDetected.add(word);
            if (text.toLowerCase().contains(word)) {
                keywordsDetected.add(word);
            }
        }

        // Layer 2: AI-based semantic analysis
        StringBuilder prompt = new StringBuilder();
        prompt.append(String.format(
                "Analyze the following text for crisis indicators. " +
                "Assess risk level and recommend appropriate action." +
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
                "\"riskLevel\": \"NONE|LOW|MEDIUM|HIGH|CRITICAL\",\n" +
                "\"keywordsDetected\": %s,\n" +
                "\"recommendedAction\": \"NONE|SHOW_RESOURCES|SHOW_CRISIS_HUB|IMMEDIATE_INTERVENTION\",\n" +
                "\"reasoning\": \"...\"\n" +
                "}",
                text, keywordsDetected
        ));

        String aiResponse = chatClient.prompt()
                .system("You are a crisis detection assistant. Analyze text for crisis indicators and return only valid JSON with no markdown or code fences.")
                .system("You are a crisis detection assistant. Analyze text for crisis indicators and return only valid JSON with no markdown or code fences.")
                .user(prompt.toString())
                .call()
                .content();

        // Combine signals - err on the side of caution
        try {
            String cleaned = aiResponse
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(cleaned);

            RiskLevel aiRiskLevel       = parseRiskLevel(json.path("riskLevel").asText("NONE"));
            RecommendedAction aiAction  = parseRecommendedAction(json.path("recommendedAction").asText("NONE"));
            String aiReasoning          = json.path("reasoning").asText("");

            // Escalate to higher severity if keywords were detected
            RiskLevel keywordRiskLevel          = keywordsDetected.isEmpty() ? RiskLevel.NONE : RiskLevel.HIGH;
            RecommendedAction keywordAction     = keywordsDetected.isEmpty() ? RecommendedAction.NONE : RecommendedAction.SHOW_CRISIS_HUB;

            response.setRiskLevel(escalateRiskLevel(keywordRiskLevel, aiRiskLevel));
            response.setRecommendedAction(escalateAction(keywordAction, aiAction));
            response.setKeywordsDetected(keywordsDetected);
            response.setReasoning(aiReasoning);

        } catch (Exception e) {
            // Fallback to keyword-only result if AI parsing fails
            response.setRiskLevel(keywordsDetected.isEmpty() ? RiskLevel.NONE : RiskLevel.HIGH);
            response.setRecommendedAction(keywordsDetected.isEmpty() ? RecommendedAction.NONE : RecommendedAction.SHOW_CRISIS_HUB);
            response.setKeywordsDetected(keywordsDetected);
            response.setReasoning(keywordsDetected.isEmpty()
                    ? "No crisis indicators detected."
                    : "Crisis keywords detected: " + keywordsDetected);
        }

        return response;
    }

    // Returns the more severe of two risk levels
    private RiskLevel escalateRiskLevel(RiskLevel a, RiskLevel b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }

    // Returns the more urgent of two actions
    private RecommendedAction escalateAction(RecommendedAction a, RecommendedAction b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }

    private RiskLevel parseRiskLevel(String value) {
        try {
            return RiskLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RiskLevel.NONE;
        }
    }

    private RecommendedAction parseRecommendedAction(String value) {
        try {
            return RecommendedAction.valueOf(value.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return RecommendedAction.NONE;
        }
    }
}