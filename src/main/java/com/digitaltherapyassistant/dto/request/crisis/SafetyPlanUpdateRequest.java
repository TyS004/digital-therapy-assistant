package com.digitaltherapyassistant.dto.request.crisis;

import lombok.Data;

import java.util.UUID;

@Data
public class SafetyPlanUpdateRequest {

    private UUID userId ;

    private String safetyPlan ;

}
