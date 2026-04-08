package com.digitaltherapyassistant.cli.api.crisis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.dto.response.ApiResponse;
import com.digitaltherapyassistant.dto.response.crisis.CopingStrategyResponse;
import com.digitaltherapyassistant.dto.response.crisis.CrisisHubResponse;
import com.digitaltherapyassistant.dto.response.crisis.SafetyPlanResponse;
import com.digitaltherapyassistant.dto.response.crisis.TrustedContactResponse;

@ExtendWith(MockitoExtension.class)
public class CrisisAPIClientImplTest {
    @Mock private RestTemplate restTemplate;
    @Mock private CLISession session;

    private CrisisAPIClientImpl crisisAPIClient;

    @BeforeEach
    void setup() {
        crisisAPIClient = new CrisisAPIClientImpl(restTemplate, session, "http://localhost:8080");
    }

    @Test
    public void testGetCrisisHub() {
        UUID userId = UUID.randomUUID();
        ApiResponse<CrisisHubResponse> response = ApiResponse.success("ok", new CrisisHubResponse());
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/crisis?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<CrisisHubResponse>>>any()))
            .thenReturn(ResponseEntity.ok(response));

        crisisAPIClient.getCrisisHub(userId);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/api/crisis?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<CrisisHubResponse>>>any());
    }

    @Test
    public void testGetCopingStrategies() {
        CopingStrategyResponse strategy = new CopingStrategyResponse();
        strategy.setName("Breathing");
        strategy.setDescription("Slow breaths");
        ApiResponse<List<CopingStrategyResponse>> response = ApiResponse.success("ok", List.of(strategy));

        when(restTemplate.exchange(
            eq("http://localhost:8080/api/crisis/coping-strategies"),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<List<CopingStrategyResponse>>>>any()))
            .thenReturn(ResponseEntity.ok(response));

        crisisAPIClient.getCopingStrategies();
        verify(restTemplate).exchange(
            eq("http://localhost:8080/api/crisis/coping-strategies"),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<List<CopingStrategyResponse>>>>any());
    }

    @Test
    public void testGetSafetyPlan() {
        UUID userId = UUID.randomUUID();
        SafetyPlanResponse plan = new SafetyPlanResponse();
        plan.setSafetyPlan("Call support");
        ApiResponse<SafetyPlanResponse> response = ApiResponse.success("ok", plan);

        when(restTemplate.exchange(
            eq("http://localhost:8080/api/crisis/safety-plan?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<SafetyPlanResponse>>>any()))
            .thenReturn(ResponseEntity.ok(response));

        crisisAPIClient.getSafetyPlan(userId);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/api/crisis/safety-plan?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<SafetyPlanResponse>>>any());
    }

    @Test
    void getCrisisHubNullResponseAndNullData() {
        UUID userId = UUID.randomUUID();
        ApiResponse<CrisisHubResponse> okNoData = ApiResponse.success("ok", null);
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/crisis?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<CrisisHubResponse>>>any()))
                .thenReturn(ResponseEntity.ok(null))
                .thenReturn(ResponseEntity.ok(okNoData));
        crisisAPIClient.getCrisisHub(userId);
        crisisAPIClient.getCrisisHub(userId);
    }

    @Test
    void getCrisisHubWithContactsAndSafetyPlan() {
        UUID userId = UUID.randomUUID();
        TrustedContactResponse c = new TrustedContactResponse();
        c.setName("Ann");
        c.setPhone("555");
        CrisisHubResponse hub = new CrisisHubResponse();
        hub.setTrustedContacts(List.of(c));
        SafetyPlanResponse sp = new SafetyPlanResponse();
        sp.setSafetyPlan("plan text");
        hub.setSafetyPlan(sp);

        ApiResponse<CrisisHubResponse> response = ApiResponse.success("ok", hub);
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/crisis?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<CrisisHubResponse>>>any()))
                .thenReturn(ResponseEntity.ok(response));
        crisisAPIClient.getCrisisHub(userId);
    }

    @Test
    void getCrisisHubTrustedContactsNull() {
        UUID userId = UUID.randomUUID();
        CrisisHubResponse hub = new CrisisHubResponse();
        hub.setTrustedContacts(null);
        hub.setSafetyPlan(null);
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/crisis?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<CrisisHubResponse>>>any()))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("ok", hub)));
        crisisAPIClient.getCrisisHub(userId);
    }

    @Test
    void getCopingStrategiesNullResponseOrData() {
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/crisis/coping-strategies"),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<List<CopingStrategyResponse>>>>any()))
                .thenReturn(ResponseEntity.ok(null))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("x", null)));
        crisisAPIClient.getCopingStrategies();
        crisisAPIClient.getCopingStrategies();
    }

    @Test
    void getSafetyPlanNullResponseOrData() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/crisis/safety-plan?userId=" + userId),
                eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<SafetyPlanResponse>>>any()))
                .thenReturn(ResponseEntity.ok(null))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("x", null)));
        crisisAPIClient.getSafetyPlan(userId);
        crisisAPIClient.getSafetyPlan(userId);
    }
}
