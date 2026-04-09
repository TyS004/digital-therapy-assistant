package com.digitaltherapyassistant.dto.response.session;

import java.time.LocalDateTime;
import java.util.UUID;

import com.digitaltherapyassistant.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionSummary {
    private UUID sessionId;
    private Status status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private int moodBefore;
    private int moodAfter;
    private String reason;
    private String aiSummary;
    private String message;
}