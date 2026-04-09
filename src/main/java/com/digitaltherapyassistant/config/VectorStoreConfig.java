package com.digitaltherapyassistant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

// Taken from Commission Calculator Example

@Configuration
public class VectorStoreConfig {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreConfig.class);

    @Bean
    public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel,
                                         @Value("${app.vectorstore.file-path:data/vectorstore.json}") String vectorStoreFilePath) {
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();

        File storeFile = new File(vectorStoreFilePath);
        if (storeFile.exists()) {
            log.info("Loading persisted vector store from: {}", storeFile.getAbsolutePath());
            store.load(storeFile);
            log.info("Vector store loaded successfully from disk");
        } else {
            log.info("No persisted vector store found at: {}. Will create on first document load.",
                    storeFile.getAbsolutePath());
        }
        return store;
    }
}