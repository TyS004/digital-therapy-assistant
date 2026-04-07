package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import org.springframework.ai.chat.client.ChatClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CrisisDetector {
    private static final Set<String> CRISIS_KEYWORDS = Set.of(
            "suicide", "kill myself", "end it all", "no reason to live",
            "better off dead", "can't go on", "want to die", "hurt myself"
    ) ;

    private final ChatClient chatClient ;

    public CrisisDetector(ChatClient chatClient) {
        this.chatClient = chatClient;
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
        // combine signals - err on the side of caution

        return new CrisisDetectionResponse() ;
    }
}