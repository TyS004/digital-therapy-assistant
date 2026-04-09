package com.digitaltherapyassistant.dto.request.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class SafetyPlanUpdateRequest {

    @NotBlank(message = "userId is required")
    private UUID userId ;

    @NotBlank(message = "safetyPlan text is required")
    private String safetyPlan ;

}
