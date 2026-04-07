package com.digitaltherapyassistant.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SafetyPlanUpdateRequest {

    private UUID userId ;

    private String safetyPlan ;

}
