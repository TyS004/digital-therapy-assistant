package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CrisisDetector {
    private static final Set<String> CRISIS_KEYWORDS = Set.of(
            "suicide", "kill myself", "end it all", "no reason to live",
            "better off dead", "can't go on", "want to die", "hurt myself"
    ) ;

    private final ChatClient chatClient ;
    private final RagContextBuilder ragContextBuilder ;

    public CrisisDetector(ChatClient chatClient, RagContextBuilder ragContextBuilder) {

        this.chatClient = chatClient;
        this.ragContextBuilder = ragContextBuilder ;
    }

    public CrisisDetectionResponse analyze(String text) {

        List<String> keywordsDetected = new ArrayList<>() ;

        // layer 1: keyword based detection
        for (String word : CRISIS_KEYWORDS) {
            if (text.contains(word)) {
                keywordsDetected.add(word) ;
            }
        }

        // layer 2: AI based semantic analysis
        StringBuilder prompt = new StringBuilder() ;
        prompt.append(String.format("Analyze the following text for crisis indicators. " +
                "Assess risk level and recommended appropriate action." +
                "\nText: %s\nEvaluate for:\n" +
                "Suicidal ideation or self-harm mentions, " +
                "Expressions of hopelessness, " +
                "Statements about being a burden, " +
                "Plans or intentions to harm self/others, or" +
                "Severe Emotional distress\n\nReturn a JSON in the following format:\n" +
                "{\n" +
                    "\"riskLevel\": \"none|low|medium|high|critical\",\n" +
                    "\"keywordsDetected\": %s,\n" +
                    "\"recommendedAction\": \"none|show_resources|show_crisis_hub|immediate_intervention\",\n" +
                    "\"reasoning\": \"...\"\n" +
                "}"
        , text, keywordsDetected));

        String aiResponse = chatClient.prompt()
                .system("You are a crisis detection assistant. Analyze text for crisis indicators and return only valid JSON.")
                .user(prompt.toString())
                .call()
                .content();

        // combine signals - err on the side of caution
        // TODO: parse aiResponse JSON into CrisisDetectionResponse fields

        return new CrisisDetectionResponse() ;
    }
}