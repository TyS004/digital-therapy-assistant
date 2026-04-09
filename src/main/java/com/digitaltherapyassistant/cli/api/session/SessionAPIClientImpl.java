package com.digitaltherapyassistant.cli.api.session;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.cli.api.APIClient;
import com.digitaltherapyassistant.dto.request.session.StartSessionRequest;
import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.SessionDto;
import com.digitaltherapyassistant.dto.response.session.SessionHistoryEntry;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.service.SessionService;

@Component
public class SessionAPIClientImpl extends APIClient implements SessionAPIClient{
    private final SessionService sessionService;

    public SessionAPIClientImpl(RestTemplate restTemplate, CLISession session, 
            @Value("${cli.api.base-url}") String clientURL, SessionService sessioService){
        super(restTemplate, session, clientURL);
        this.sessionService = sessioService;
    }

    public void getSessionLibrary(UUID userId){
        List<SessionModuleDto> response =
            GET(clientURL + "/api/sessions?userId=" + userId,
                new ParameterizedTypeReference<List<SessionModuleDto>>() {});

        if(response == null) { return; }

        System.out.println("\n=== Session Library ===");
        for (SessionModuleDto module : response) {
            System.out.println("\nModule: " + module.getId());
            System.out.println("  Sessions:");
            for (SessionDto session : module.getSessions()) {
                System.out.printf("    [%d] %s%n", session.getOrderIndex(), session.getTitle());
                System.out.printf("        Duration : %d min%n", session.getDurationMinutes());
                System.out.printf("        Modalities: %s%n", session.getModalities());
                if (session.getObjectives() != null && !session.getObjectives().isEmpty()) {
                    System.out.println("        Objectives:");
                    session.getObjectives().forEach(o -> System.out.println("          - " + o));
                }
            }
        }
    }

    public void startSession(UUID userId, UUID sessionId){
        StartSessionRequest request = new StartSessionRequest();
        request.setUserId(userId);
        
        ActiveSession response = 
            POST(clientURL + "/api/sessions/" + sessionId + "/start", 
                request, ActiveSession.class);
        if(response == null) { return; }
        System.out.println("Started Session");
    }

    public void getSessionHistory(UUID userId){
        List<SessionHistoryEntry> response = sessionService.getSessionHistory(userId);

        if(response == null) {  System.out.println("No Session History Found"); return; }
        
        System.out.println("\n=== Session History ===");
        for (SessionHistoryEntry entry : response) {
            System.out.println("\n  Session ID : " + entry.getSessionId());
            System.out.println("  Status     : " + entry.getStatus());
            System.out.println("  Started    : " + entry.getStartedAt());
            System.out.println("  Ended      : " + (entry.getEndedAt() != null ? entry.getEndedAt() : "In Progress"));
            System.out.printf( "  Mood       : %d → %d%n", entry.getMoodBefore(), entry.getMoodAfter());
            if (entry.getMessage() != null && !entry.getMessage().isBlank()) {
                System.out.println("  Message    : " + entry.getMessage());
            }
            System.out.println("  " + "-".repeat(40));
        }
    }
}
