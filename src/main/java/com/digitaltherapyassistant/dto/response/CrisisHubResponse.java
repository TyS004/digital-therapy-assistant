package com.digitaltherapyassistant.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CrisisHubResponse {

    private List<TrustedContactResponse> trustedContacts ;
    private List<CopingStrategyResponse> copingStrategies ;
    private SafetyPlanResponse safetyPlan ;

}
