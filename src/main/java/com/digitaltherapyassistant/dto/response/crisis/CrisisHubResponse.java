package com.digitaltherapyassistant.dto.response.crisis;

import lombok.Data;

import java.util.List;

@Data
public class CrisisHubResponse {

    private List<TrustedContactResponse> trustedContacts ;
    private List<CopingStrategyResponse> copingStrategies ;
    private SafetyPlanResponse safetyPlan ;

}
