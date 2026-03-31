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
        for(SessionModuleDto module : response){
            System.out.println("Module ID: " + module.getModule().getId());
        }
    }

    public void startSession(UUID userId, UUID sessionId){
        StartSessionRequest request = new StartSessionRequest();
        request.setUserId(userId);
        
        ActiveSession response = 
            POST(clientURL + "/api/sessions/" + sessionId + "/start", 
                request, ActiveSession.class);
        if(response == null) { return; }
    }

    public void getSessionHistory(UUID userId){
        List<SessionHistoryEntry> response = sessionService.getSessionHistory(userId);

        if(response == null) { return; }
        for(SessionHistoryEntry session : response){
            System.out.println("Session ID: " + session.getUserSession().getId());
        }
    }
}
