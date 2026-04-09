package com.digitaltherapyassistant.dto.request.session;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartSessionRequest {
    @NotNull(message = "UserId required")
    private UUID userId;
}
