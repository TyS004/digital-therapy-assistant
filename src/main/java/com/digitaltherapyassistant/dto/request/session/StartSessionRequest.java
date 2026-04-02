package com.digitaltherapyassistant.dto.request.session;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartSessionRequest {
    private UUID userId;
}
