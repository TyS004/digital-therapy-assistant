package com.digitaltherapyassistant.dto.response.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SafetyPlanResponse {

    @NotBlank(message = "safety plan required")
    private String safetyPlan ;

}
