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
public class ActiveSession {
    private UUID sessionId;
    private String title;
    private Status status;   
    private LocalDateTime startedAt;
    private int moodBefore;
    private String message;
}
