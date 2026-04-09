package com.digitaltherapyassistant.dto.response.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CrisisHubResponse {

    @NotBlank(message = "trusted contacts required")
    private List<TrustedContactResponse> trustedContacts ;

    @NotBlank(message = "coping strategies required")
    private List<CopingStrategyResponse> copingStrategies ;

    @NotBlank(message = "safety plan required")
    private SafetyPlanResponse safetyPlan ;

}
