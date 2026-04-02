package com.digitaltherapyassistant.dto.response.session;

import com.digitaltherapyassistant.entity.CbtSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionSummary {
    private CbtSession session;
    private String reason;
    private String message;
}