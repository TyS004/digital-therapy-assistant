package com.digitaltherapyassistant.cli.api.diary;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.cli.api.APIClient;
import com.digitaltherapyassistant.dto.DiaryEntryCreate;
import com.digitaltherapyassistant.dto.DiaryEntryResponse;
import com.digitaltherapyassistant.dto.DiaryEntrySummary;
import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.RestPageResponse;

@Component
public class DiaryAPIClientImpl extends APIClient implements DiaryAPIClient{
    public DiaryAPIClientImpl(RestTemplate restTemplate, CLISession session,
        @Value("${cli.api.base-url}") String clientURL){
        super(restTemplate, session, clientURL);
    }

    public void createEntry(UUID userId, DiaryEntryCreate request){
        DiaryEntryResponse response =
            POST(clientURL + "/diary/entries?userId=" + userId, request, DiaryEntryResponse.class);
        if (response == null) {
            return;
        }

        System.out.println("Created entry ID: " + response.getId());
    }

    @Override
    public void getEntries(UUID userId, Pageable pageable) {
        RestPageResponse<DiaryEntrySummary> response = GET(
            clientURL + "/diary/entries?userId=" + userId
                + "&page=" + pageable.getPageNumber()
                + "&size=" + pageable.getPageSize(),
            new ParameterizedTypeReference<RestPageResponse<DiaryEntrySummary>>() {}
        );

        if (response == null || response.getContent().isEmpty()) {
            System.out.println("No diary entries found.");
            return;
        }
    
        System.out.println("\n=== Diary Entries (Page " + (response.getNumber() + 1) 
            + " of " + response.getTotalPages() + ") ===");
        
        for (DiaryEntrySummary entry : response.getContent()) {
            System.out.println("----------------------------------");
            System.out.println("ID:               " + entry.getId());
            System.out.println("Situation:        " + entry.getSituation());
            System.out.println("Automatic Thought:" + entry.getAutomaticThought());
            System.out.println("Mood Before:      " + entry.getMoodBefore());
            System.out.println("Mood After:       " + entry.getMoodAfter());
            System.out.println("Created At:       " + entry.getCreatedAt());
        }
        System.out.println("----------------------------------");
        System.out.println("Total Entries: " + response.getTotalElements());
    }

    @Override
    public void getInsights(UUID userId) {
        DiaryInsights response = GET(
            clientURL + "/diary/insights?userId=" + userId,
            DiaryInsights.class
        );
        if (response == null) {
            return;
        }

        System.out.println("\n=== Diary Insights ===");
        System.out.println("Total Entries: " + response.getTotalEntries());
        System.out.println("Average Mood Improvement: " + response.getAverageMoodImprovement());
        System.out.println("Summary: " + response.getSummary());
        if (response.getTopDistortions() != null) {
            response.getTopDistortions().forEach(distortion ->
                System.out.println("- " + distortion.getName() + ": " + distortion.getCount()));
        }
    }
}
