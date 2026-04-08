package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.entity.CbtSession;
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
        context.append(String.format("Relevant Past Session Data: %s\n", this.embeddingService.extractContext(relevantPastSessionData))) ;

        // 3. get user recent session history
        StringBuilder recentSessionString = new StringBuilder() ;
        recentSessionString.append(String.format("Recent Session History:\n")) ;
        List<UserSession> recentSessionHistory = this.sessionRepository.findRecentSessionHistoryByUserId(userId).stream().limit(3).toList() ;
        for (UserSession session : recentSessionHistory) {
            CbtSession cbtSession = session.getCbtSession() ;
            recentSessionString.append(String.format("Session Topic: %s\nDescription: %s\nObjectives: %s\nStart Time: %s\nEnd Time: %s\nMood Before: %s\nMood After: %s\n", cbtSession.getTitle(), cbtSession.getDescription(), cbtSession.getObjectives(), session.getStartedAt(), session.getEndedAt(), session.getMoodBefore(), session.getMoodAfter())) ;
        }
        context.append(recentSessionString.toString()) ;


        // 4. get user diary patterns
        List<CognitiveDistortion> topDistortions = this.diaryRepository.findTopDistortionsByUser(userId, PageRequest.of(0, 3)).stream().toList();
        context.append(String.format("Top User Cognitive Distortions: %s\n", topDistortions)) ;

        Integer averageMoodImprovement = this.diaryRepository.getAverageMoodImprovement() ;
        context.append(String.format("Average User Mood Improvement From Diaries: %s\n", averageMoodImprovement)) ;


        // 5. get current session transcript
//        UserSession session = this.sessionRepository.findSessionWithChatMessages(sessionId).orElseThrow(() -> new ResourceNotFoundException("UserSession", "id", sessionId.toString())) ;
        List<ChatMessage> chatMessages = this.chatMessageRepository.findByUserSessionIdOrderByTimestampAsc(sessionId) ;
        StringBuilder transcript = new StringBuilder() ;
        for (ChatMessage message : chatMessages) {
            transcript.append(String.format("%s: %s\n", message.getRole(), message.getContent())) ;
        }
        context.append(String.format("Current Session Transcript: %s\n", transcript.toString())) ;

        return context.toString() ;
    }
}