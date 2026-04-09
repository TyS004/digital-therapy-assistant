package com.digitaltherapyassistant.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Session", description = "Session REST API Endpoints")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @Operation(summary="get session library", description="get library of sessions that a user is working on")
    @GetMapping("")
    public ResponseEntity<List<SessionModuleDto>> getSessionLibrary(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId){
        List<SessionModuleDto> response = sessionService.getSessionLibrary(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="get the details of a session", description="get all session details related to a specific session")
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDetail> getSessionDetails(
            @Parameter(description = "filter by session id")
            @PathVariable UUID sessionId){
        SessionDetail response = sessionService.getSessionDetails(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="start a user session", description="start a brand new user session")
    @PostMapping("/{sessionId}/start")
    public ResponseEntity<ActiveSession> startSession(
            @Parameter(description = "filter by session id")
            @PathVariable UUID sessionId,
            @Parameter(description = "start a user session with userId")
            @Valid @RequestBody StartSessionRequest request){
        ActiveSession response = sessionService.startSession(request.getUserId(), sessionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="chat with the chatbot", description="send a message to the chatbot and receive a response")
    @PostMapping("/{sessionId}/chat")
    public ResponseEntity<ChatResponse> chat(
            @Parameter(description = "filter by session id")
            @PathVariable UUID sessionId,
            @Parameter(description = "a string message to send to the chat client")
            @RequestBody String message){
        ChatResponse response = sessionService.chat(sessionId, message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="end a user session", description="end a user session and provide reason for ending")
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<SessionSummary> endSession(
            @Parameter(description = "filter by session id")
            @PathVariable UUID sessionId,
            @Parameter(description = "string reason for why the session ended")
            @RequestBody String reason){
        SessionSummary response = sessionService.endSession(sessionId, reason);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
