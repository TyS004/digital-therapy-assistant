package com.digitaltherapyassistant.service.rag;

import com.digitaltherapyassistant.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KnowledgeBaseLoader {
    private final VectorStore vectorStore;
    private final String storePath;
    private final String burnoutContentPath ;
    private final String cbtTechniquesPath ;
    private final String crisisProtocolPath ;
    private final String distortionsPath ;

    public KnowledgeBaseLoader(VectorStore vectorStore,
                               @Value("${spring.ai.vectorstore.simple.store.path:./data/vectorstore.json}") String storePath,
                               @Value("${app.knowledge-base.burnout-content.file-path:/knowledge-base/burnout-content.json") String burnoutContentPath,
                               @Value("${app.knowledge-base.cbt-techniques.file-path:/knowledge-base/cbt-techniques.json") String cbtTechniquesPath,
                               @Value("${app.knowledge-base.crisis-protocol.file-path:/knowledge-base/crisis-protocol.json") String crisisProtocolPath,
                               @Value("${app.knowledge-base.distortions.file-path:/knowledge-base/distortions.json") String distortionsPath
                               ) {
        this.vectorStore = vectorStore;
        this.storePath = storePath;
        this.burnoutContentPath = burnoutContentPath ;
        this.cbtTechniquesPath = cbtTechniquesPath ;
        this.crisisProtocolPath = crisisProtocolPath ;
        this.distortionsPath = distortionsPath ;
    }

    @PostConstruct
    public void loadKnowledgeBase() throws Exception {
        File storeFile = new File(storePath);

        if (storeFile.exists() && storeFile.length() > 0) {
            System.out.println("Vector Store already persisted at " + storeFile.getAbsolutePath() + ". Skipping knowledge base reload.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        int totalLoaded = 0;

        List<Document> documents = new ArrayList<>() ;

        // 1. Load Distortions
        documents.addAll(this.loadDistortions(mapper)) ;

        // 2. Load CBT techniques
        documents.addAll(this.loadCbtTechniques(mapper)) ;

        // 3. Load Crisis Protocols
        documents.addAll(this.loadCrisisProtocols(mapper)) ;

        // 4. Load Burnout Specific Content
        documents.addAll(this.loadBurnoutContent(mapper)) ;

        totalLoaded = 4 ;

        if (!documents.isEmpty()) {
            vectorStore.add(documents) ;
            System.out.println("Successfully loaded " + totalLoaded + " documents") ;
        }

        System.out.println("Knowledge base loaded: " + totalLoaded + " total documents in vector store");

        if (vectorStore instanceof SimpleVectorStore svs) {
            storeFile.getParentFile().mkdirs();
            svs.save(storeFile);
            System.out.println("Vector store persisted to: " + storeFile.getAbsolutePath());
        }
    }


    private List<Document> loadBurnoutContent(ObjectMapper mapper) throws Exception {
        List<Document> documents = new ArrayList<>() ;

        List<BurnoutContent> burnoutContents = mapper.readValue(new File(this.burnoutContentPath), new TypeReference<List<BurnoutContent>>(){}) ;

        for (BurnoutContent burnoutContent : burnoutContents) {

            // natural language content for vector storage
            String content = String.format(
                    "Burnout Content titled '%s' with description '%s'. " +
                            "Examples of this distortion are '%s' ." +
                            "Some reframing questions to use are '%s' .",
                    burnoutContent.getName(),
                    burnoutContent.getDescription(),
                    burnoutContent.getExamples(),
                    burnoutContent.getReframingQuestions()

            ) ;

            // add metadata for fast filtering
            Map<String, Object> metadata = new HashMap<>() ;
            metadata.put("type", "burnoutContent") ;
            metadata.put("id", burnoutContent.getId()) ;

            documents.add(new Document(content, metadata)) ;
        }



        return documents ;
    }


    private List<Document> loadCbtTechniques(ObjectMapper mapper) throws Exception {
        List<Document> documents = new ArrayList<>() ;
        List<CbtTechnique> cbtTechniques = mapper.readValue(new File(this.cbtTechniquesPath), new TypeReference<List< CbtTechnique >>(){}) ;

        for (CbtTechnique cbtTechnique : cbtTechniques) {
            // natural language
            String content = String.format(
                    "Cbt Technique titled '%s' is described as '%s' ." +
                            "Examples of this technique are '%s' ." +
                            "Some reframing questions to use are '%s' .",
                    cbtTechnique.getName(),
                    cbtTechnique.getDescription(),
                    cbtTechnique.getExamples(),
                    cbtTechnique.getReframingQuestions()
            ) ;

            // metadata
            Map<String, Object> metadata = new HashMap<>() ;
            metadata.put("type", "cbtTechnique") ;
            metadata.put("id", cbtTechnique.getId()) ;

            documents.add(new Document(content, metadata)) ;

        }

        return documents ;
    }

    private List<Document> loadCrisisProtocols(ObjectMapper mapper) throws Exception {
        List<Document> documents = new ArrayList<>() ;
        List<CrisisProtocol> crisisProtocols = mapper.readValue(new File(crisisProtocolPath), new TypeReference<List< CrisisProtocol >>(){}) ;

        for (CrisisProtocol crisisProtocol : crisisProtocols) {
            // natural language
            String content = String.format(
                    "Crisis Protocol titled '%s' is described as '%s' ." +
                            "Examples of this technique are '%s' ." +
                            "Some reframing questions to use are '%s' .",
                    crisisProtocol.getName(),
                    crisisProtocol.getDescription(),
                    crisisProtocol.getExamples(),
                    crisisProtocol.getReframingQuestions()
            ) ;

            // metadata
            Map<String, Object> metadata = new HashMap<>() ;
            metadata.put("type", "crisisProtocol") ;
            metadata.put("id", crisisProtocol.getId()) ;

            documents.add(new Document(content, metadata)) ;

        }

        return documents ;
    }

    private List<Document> loadDistortions(ObjectMapper mapper) throws Exception {
        List<Document> documents = new ArrayList<>() ;

        List<Distortion> distortions = mapper.readValue(new File(this.distortionsPath), new TypeReference<List<Distortion>>(){}) ;

        for (Distortion distortion : distortions) {

            // natural language content for vector storage
            String content = String.format(
                    "Distortion titled '%s' with description '%s'. " +
                            "Examples of this distortion are '%s' ." +
                            "Some reframing questions to use are '%s' .",
                    distortion.getName(),
                    distortion.getDescription(),
                    distortion.getExamples(),
                    distortion.getReframingQuestions()

            ) ;

            // add metadata for fast filtering
            Map<String, Object> metadata = new HashMap<>() ;
            metadata.put("type", "distortion") ;
            metadata.put("id", distortion.getId()) ;

            documents.add(new Document(content, metadata)) ;
        }



        return documents ;
    }



}