package com.digitaltherapyassistant.cli.api.crisis;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public interface CrisisAPIClient {
    void getCrisisHub(UUID userId);
    void getCopingStrategies();
    void getSafetyPlan(UUID userId);
}
