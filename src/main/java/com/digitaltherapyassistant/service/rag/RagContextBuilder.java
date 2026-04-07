package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.entity.ChatMessage;
import com.digitaltherapyassistant.entity.CognitiveDistortion;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.exception.ResourceNotFoundException;
import com.digitaltherapyassistant.repository.ChatMessageRepository;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.UUID;

public class RagContextBuilder {
    private final SimpleVectorStore vectorStore ;
    private final EmbeddingService embeddingService ;
    private final UserSessionRepository sessionRepository ;
    private final DiaryEntryRepository diaryRepository ;
    private final ChatMessageRepository chatMessageRepository ;

    public RagContextBuilder(SimpleVectorStore vectorStore, EmbeddingService embeddingService, UserSessionRepository sessionRepository, DiaryEntryRepository diaryRepository, ChatMessageRepository chatMessageRepository) {
        this.vectorStore = vectorStore;
        this.embeddingService = embeddingService;
        this.sessionRepository = sessionRepository;
        this.diaryRepository = diaryRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public String buildContext(UUID userId, UUID sessionId, String query) {
        StringBuilder context = new StringBuilder() ;

        // 1 retrieve relevant CBT knowledge
        List<Document> relevantKnowledge = this.embeddingService.search(query, 5) ;
        context.append(String.format("Relevant CBT Knowledge: %s\n", this.embeddingService.extractContext(relevantKnowledge))) ;

        // 2 retrieve relevant past session data via vector similarity search
        List<Document> relevantPastSessionData = this.embeddingService.searchByTypeAndId(query, "sessionData", userId.toString(), 5) ;
        context.append(String.format("Relevant Past Session Data: %s\n", relevantPastSessionData)) ;

        // 3. get user recent session history
        List<UserSession> recentSessionHistory = this.sessionRepository.findByUserIdOrderByStartedAtDescLimit3(userId) ;


        // 4. get user diary patterns
        List<CognitiveDistortion> topDistortions = this.diaryRepository.findTopDistortionsByUser(userId, PageRequest.of(0, 3)).stream().toList();
        context.append(String.format("Top User Cognitive Distortions: %s\n", topDistortions)) ;

        Integer averageMoodImprovement = this.diaryRepository.getAverageMoodImprovement() ;
        context.append(String.format("Average User Mood Improvement From Diaries: %s\n", averageMoodImprovement)) ;


        // 5. get current session transcript
//        UserSession session = this.sessionRepository.findSessionWithChatMessages(sessionId).orElseThrow(() -> new ResourceNotFoundException("UserSession", "id", sessionId.toString())) ;
        List<ChatMessage> chatMessages = this.chatMessageRepository.findByUserSessionIdOrderByTimestampAsc(sessionId).orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "userSessionId", sessionId.toString())) ;
        StringBuilder transcript = new StringBuilder() ;
        for (ChatMessage message : chatMessages) {
            transcript.append(String.format("%s: %s\n", message.getRole(), message.getContent())) ;
        }
        context.append(String.format("Current Session Transcript: %s\n", transcript.toString())) ;

        return context.toString() ;
    }
}