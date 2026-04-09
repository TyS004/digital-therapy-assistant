package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.DistortionSuggestion;
import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.CognitiveDistortion;
import com.digitaltherapyassistant.entity.DiaryEntry;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.model.Distortion;
import com.digitaltherapyassistant.repository.CbtSessionRepository;
import com.digitaltherapyassistant.repository.CognitiveDistortionRepository;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import com.digitaltherapyassistant.service.interfaces.AiServiceInterface;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.EmbeddingService;
import com.digitaltherapyassistant.service.rag.RagContextBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiService implements AiServiceInterface {

    private final CrisisServiceInterface crisisService;
    private final RagContextBuilder ragContextBuilder;
    private final ChatClient chatClient;
    private final EmbeddingService embeddingService;
    private final DiaryEntryRepository diaryEntryRepository;
    private final CbtSessionRepository cbtSessionRepository;
    private final UserSessionRepository userSessionRepository;
    private final CognitiveDistortionRepository cognitiveDistortionRepository ;
    private static final Logger logger = LoggerFactory.getLogger(AiService.class);

    public AiService(
            CrisisServiceInterface crisisService,
            RagContextBuilder ragContextBuilder,
            ChatClient chatClient,
            EmbeddingService embeddingService,
            DiaryEntryRepository diaryEntryRepository,
            CbtSessionRepository cbtSessionRepository,
            UserSessionRepository userSessionRepository,
            CognitiveDistortionRepository cognitiveDistortionRepository) {
        this.crisisService = crisisService;
        this.ragContextBuilder = ragContextBuilder;
        this.chatClient = chatClient;
        this.embeddingService = embeddingService;
        this.diaryEntryRepository = diaryEntryRepository;
        this.cbtSessionRepository = cbtSessionRepository;
        this.userSessionRepository = userSessionRepository;
        this.cognitiveDistortionRepository = cognitiveDistortionRepository ;
    }

    @Override
    public ChatResponse generateResponse(UUID sessionId, String userMessage) {
        UserSession userSession = getUserSessionByCbtSessionId(sessionId);
        String context = this.ragContextBuilder.buildContext(
                userSession.getUser().getId(),
                userSession.getId(),
                userMessage);
        return chatClient.prompt()
                .system("You are a CBT therapy assistant. Be empathetic, concise, and practical.")
                .user("Context:\n" + context + "\n\nUser message:\n" + userMessage)
                .call()
                .chatResponse();
    }

    @Override
    public List<DistortionSuggestion> analyzeThought(String automaticThought) {
        List<DistortionSuggestion> suggestions = new ArrayList<>();
        List<CognitiveDistortion> cognitiveDistortions = cognitiveDistortionRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();

        String prompt = String.format(
                "Analyze the following automatic thought for cognitive distortions.\n" +
                        "Return a JSON array of identified distortions with confidence scores.\n\n" +
                        "Thought: %s\n\n" +
                        "Available distortion types: %s\n",
            automaticThought, cognitiveDistortions);

        String aiResponse = chatClient.prompt()
            .system("You are a CBT therapist identifying cognitive distortions. Return only valid JSON.")
            .user(prompt)
            .call()
            .content();

        try {
            suggestions = mapper.readValue(aiResponse, new TypeReference<List<DistortionSuggestion>>() {});
        } catch (Exception e) {
            logger.error("Failed to parse AI response: {}", e.getMessage());
        }

        return suggestions;
    }

    @Override
    public List<String> generateReframingPrompts(String thought, List<String> distortionIds) {
        List<String> reframings = new ArrayList<>();
        StringBuilder distortionContext = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();

        for (String id : distortionIds) {
            List<Document> docs = embeddingService.searchById(thought, id, 1);
            distortionContext.append(embeddingService.extractContext(docs));
        }

        String prompt = String.format(
            "Given this thought: '%s'\n" +
            "And these cognitive distortions that apply:\n%s\n" +
            "Suggest 3 reframing statements to help challenge this thought. " +
            "Return a JSON array of strings.",
            thought, distortionContext.toString()
        );

        String aiResponse = chatClient.prompt()
            .system("You are a CBT therapist helping reframe negative thoughts. Return only valid JSON.")
            .user(prompt)
            .call()
            .content();

        try {
            reframings = mapper.readValue(aiResponse, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            logger.error("Failed to parse reframing response: {}", e.getMessage());
        }

        return reframings;
    }

    @Override
    public CrisisDetectionResponse detectCrisis(String text) {
        return this.crisisService.detectCrisis(text);
    }

    @Override
    public DiaryInsights generateInsights(UUID userId) {
        List<DiaryEntry> entries = diaryEntryRepository.findByUserIdAndDeletedFalse(userId);
        int totalEntries = entries.size();

        Double avg = diaryEntryRepository.calculateAverageMoodImprovement(userId);
        double averageMoodImprovement = avg != null ? avg : 0.0;

        List<Object[]> rawDistortions = diaryEntryRepository.findTopDistortionsByUser(userId)
                .stream().limit(5).toList();
        List<DiaryInsights.TopDistortion> topDistortions = new ArrayList<>();
        for (Object[] row : rawDistortions) {
            CognitiveDistortion cd = (CognitiveDistortion) row[0];
            long count = (long) row[1];
            topDistortions.add(new DiaryInsights.TopDistortion(cd.getId(), cd.getName(), (int) count));
        }

        String summary = chatClient.prompt()
            .system("You are a CBT therapist providing insights on a patient's progress. Be encouraging and specific.")
            .user(String.format("User has %d diary entries, average mood improvement of %.2f. Top distortions: %s. Summarize their progress.",
                totalEntries, averageMoodImprovement,
                topDistortions.stream().map(DiaryInsights.TopDistortion::getName).collect(java.util.stream.Collectors.joining(", "))))
            .call()
            .content();

        return new DiaryInsights(totalEntries, averageMoodImprovement, topDistortions, summary);
    }

    @Override
    public SessionSummary summarizeSession(UUID sessionId) {
        UserSession userSession = getUserSessionByCbtSessionId(sessionId);
        String context = this.ragContextBuilder.buildContext(
                userSession.getUser().getId(),
                userSession.getId(),
                "Summarize this CBT session");
        String summaryText = chatClient.prompt()
                .system("You summarize CBT sessions clearly and safely.")
                .user("Create a brief summary and actionable next steps from:\n" + context)
                .call()
                .content();

        SessionSummary summary = new SessionSummary();
        summary.setSessionId(sessionId);
        summary.setAiSummary(summaryText);
        return summary;
    }

    private UserSession getUserSessionByCbtSessionId(UUID sessionId) {
        CbtSession cbtSession = cbtSessionRepository.findById(sessionId)
                .orElseThrow(() -> new DigitalTherapyException("CBT Session Not Found"));

        return userSessionRepository.findByCbtSession(cbtSession)
                .orElseThrow(() -> new DigitalTherapyException("User Session Not Found"));
    }
}
