package com.digitaltherapyassistant.service.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

import java.util.List;

public class EmbeddingService {

    private VectorStore vectorStore ;

    public EmbeddingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore ;
    }

    public List<Document> search(String query, int topK) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.5)
                .build() ;

        List<Document> searchResults = vectorStore.similaritySearch(searchRequest) ;

        return searchResults ;
    }

    public List<Document> searchByType(String query, String documentType, int topK) {
        FilterExpressionBuilder builder = new FilterExpressionBuilder() ;
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.5)
                .filterExpression(builder.eq("type", documentType).build())
                .build() ;

        List<Document> searchResults = vectorStore.similaritySearch(searchRequest) ;

        return searchResults ;
    }

    public List<Document> searchById(String query, String id, int topK) {
        FilterExpressionBuilder builder = new FilterExpressionBuilder() ;
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.5)
                .filterExpression(builder.eq("id", id).build())
                .build() ;

        List<Document> searchResults = vectorStore.similaritySearch(searchRequest) ;

        return searchResults ;
    }

    public List<Document> searchByTypeAndId(String query, String type, String id, int topK) {
        FilterExpressionBuilder builder = new FilterExpressionBuilder() ;
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.5)
                .filterExpression(builder.and(builder.eq("type", type), builder.eq("id", id)).build())
                .build();

        List<Document> searchResult = vectorStore.similaritySearch(searchRequest) ;

        return searchResult ;
    }

    public String extractContext(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return "no information found" ;
        }

        StringBuilder context = new StringBuilder() ;
        for (int i = 0 ; i < documents.size() ; ++i) {
            Document document = documents.get(i) ;
            context.append(String.format("Document %d: %s\n", (i+1), document.getText())) ;
        }

        return context.toString() ;
    }
}
