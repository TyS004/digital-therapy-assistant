package com.digitaltherapyassistant.cli.api.crisis;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.cli.api.APIClient;
import com.digitaltherapyassistant.dto.response.ApiResponse;
import com.digitaltherapyassistant.dto.response.crisis.CopingStrategyResponse;
import com.digitaltherapyassistant.dto.response.crisis.CrisisHubResponse;
import com.digitaltherapyassistant.dto.response.crisis.SafetyPlanResponse;

@Component
public class CrisisAPIClientImpl extends APIClient implements CrisisAPIClient {
    public CrisisAPIClientImpl(RestTemplate restTemplate, CLISession session,
            @Value("${cli.api.base-url}") String clientURL) {
        super(restTemplate, session, clientURL);
    }

    @Override
    public void getCrisisHub(UUID userId) {
        ApiResponse<CrisisHubResponse> response = GET(
                clientURL + "/api/crisis?userId=" + userId,
                new ParameterizedTypeReference<ApiResponse<CrisisHubResponse>>() {});
        if (response == null || response.getData() == null) {
            return;
        }

        CrisisHubResponse data = response.getData();
        System.out.println("\n=== Emergency Resources ===");
        if (data.getTrustedContacts() != null) {
            System.out.println("Trusted Contacts: " + data.getTrustedContacts().size());
            data.getTrustedContacts().forEach(contact ->
                    System.out.println("- " + contact.getName() + " (" + contact.getPhone() + ")"));
        }
        if (data.getSafetyPlan() != null) {
            System.out.println("Safety Plan: " + data.getSafetyPlan().getSafetyPlan());
        }
    }

    @Override
    public void getCopingStrategies() {
        ApiResponse<List<CopingStrategyResponse>> response = GET(
                clientURL + "/api/crisis/coping-strategies",
                new ParameterizedTypeReference<ApiResponse<List<CopingStrategyResponse>>>() {});
        if (response == null || response.getData() == null) {
            return;
        }

        System.out.println("\n=== Coping Strategies ===");
        for (CopingStrategyResponse strategy : response.getData()) {
            System.out.println("- " + strategy.getName() + ": " + strategy.getDescription());
        }
    }

    @Override
    public void getSafetyPlan(UUID userId) {
        ApiResponse<SafetyPlanResponse> response = GET(
                clientURL + "/api/crisis/safety-plan?userId=" + userId,
                new ParameterizedTypeReference<ApiResponse<SafetyPlanResponse>>() {});
        if (response == null || response.getData() == null) {
            return;
        }

        System.out.println("\n=== Safety Plan ===");
        System.out.println(response.getData().getSafetyPlan());
    }
}
