package com.digitaltherapyassistant.controller;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitaltherapyassistant.dto.request.session.StartSessionRequest;
import com.digitaltherapyassistant.dto.response.session.ActiveSession;
import com.digitaltherapyassistant.dto.response.session.ChatResponse;
import com.digitaltherapyassistant.dto.response.session.SessionDetail;
import com.digitaltherapyassistant.dto.response.session.SessionModuleDto;
import com.digitaltherapyassistant.dto.response.session.SessionSummary;
import com.digitaltherapyassistant.service.SessionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Session", description = "Session REST API Endpoints")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @GetMapping("")
    public ResponseEntity<List<SessionModuleDto>> getSessionLibrary(@RequestParam UUID userId){
        List<SessionModuleDto> response = sessionService.getSessionLibrary(userId);
        if(response.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }   
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDetail> getSessionDetails(@PathVariable UUID sessionId){
        SessionDetail response = sessionService.getSessionDetails(sessionId);
        if(response.getSession() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{sessionId}/start")
    public ResponseEntity<ActiveSession> startSession(@PathVariable UUID sessionId, @RequestBody StartSessionRequest request){                                                            
        ActiveSession response = sessionService.startSession(request.getUserId(), sessionId);
        if(response.getSession() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{sessionId}/chat")
    public ResponseEntity<ChatResponse> chat(@PathVariable UUID sessionId, @RequestBody String message){
        ChatResponse response = sessionService.chat(sessionId, message);
        if(response.getChatMessage() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<SessionSummary> endSession(@PathVariable UUID sessionId, @RequestBody String reason){
        SessionSummary response = sessionService.endSession(sessionId, reason);
        if(response.getSession() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
