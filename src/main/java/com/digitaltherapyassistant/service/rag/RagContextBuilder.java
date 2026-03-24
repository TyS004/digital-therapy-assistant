package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.repository.ChatMessageRepository;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import com.digitaltherapyassistant.service.rag.EmbeddingService;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

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
        // 2 retrieve relevant past session data via vector similarity search
        // 3. get user recent session history
        // 4. get user diary patterns
        // 5. get current session transcript

        return context.toString() ;
    }
}