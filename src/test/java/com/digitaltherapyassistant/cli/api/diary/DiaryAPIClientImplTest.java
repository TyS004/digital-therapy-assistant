package com.digitaltherapyassistant.cli.api.diary;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.dto.DiaryEntryCreate;
import com.digitaltherapyassistant.dto.DiaryEntryResponse;
import com.digitaltherapyassistant.dto.DiaryEntrySummary;
import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.RestPageResponse;

@ExtendWith(MockitoExtension.class)
public class DiaryAPIClientImplTest {
    @Mock private RestTemplate restTemplate;
    @Mock private CLISession session;

    private DiaryAPIClientImpl diaryAPIClient;

    @BeforeEach
    void setup() {
        diaryAPIClient = new DiaryAPIClientImpl(restTemplate, session, "http://localhost:8080");
    }

    @Test
    public void testCreateEntry() {
        UUID userId = UUID.randomUUID();
        DiaryEntryCreate request = new DiaryEntryCreate();
        DiaryEntryResponse response = new DiaryEntryResponse(
            UUID.randomUUID(), userId, "s", "a", List.of(), List.of(), "alt", 5, 7, 70, 40, null);

        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId),
            eq(HttpMethod.POST), any(), eq(DiaryEntryResponse.class)))
            .thenReturn(ResponseEntity.ok(response));

        diaryAPIClient.createEntry(userId, request);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId),
            eq(HttpMethod.POST), any(), eq(DiaryEntryResponse.class));
    }

    @Test
    public void testGetEntries() {
        UUID userId = UUID.randomUUID();
        RestPageResponse<DiaryEntrySummary> page = new RestPageResponse<>();
        page.setContent(List.of(new DiaryEntrySummary(UUID.randomUUID(), "s", "a", 5, 6, List.of(), null)));
        page.setNumber(0);
        page.setTotalPages(1);
        page.setTotalElements(1);

        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId + "&page=0&size=10"),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<RestPageResponse<DiaryEntrySummary>>>any()))
            .thenReturn(ResponseEntity.ok(page));

        diaryAPIClient.getEntries(userId, PageRequest.of(0, 10));
        verify(restTemplate).exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId + "&page=0&size=10"),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<RestPageResponse<DiaryEntrySummary>>>any());
    }

    @Test
    public void testGetInsights() {
        UUID userId = UUID.randomUUID();
        DiaryInsights insights = new DiaryInsights(3, 1.5, List.of(), "Good progress");
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/insights?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(DiaryInsights.class)))
            .thenReturn(ResponseEntity.ok(insights));

        diaryAPIClient.getInsights(userId);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/api/diary/insights?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(DiaryInsights.class));
    }

    @Test
    public void createEntryNullResponse() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId),
            eq(HttpMethod.POST), any(), eq(DiaryEntryResponse.class)))
            .thenReturn(ResponseEntity.ok(null));

        diaryAPIClient.createEntry(userId, new DiaryEntryCreate());
    }

    @Test
    public void getEntriesNullResponse() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId + "&page=0&size=10"),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<RestPageResponse<DiaryEntrySummary>>>any()))
            .thenReturn(ResponseEntity.ok(null));

        diaryAPIClient.getEntries(userId, PageRequest.of(0, 10));
    }

    @Test
    public void getEntriesEmptyContent() {
        UUID userId = UUID.randomUUID();
        RestPageResponse<DiaryEntrySummary> page = new RestPageResponse<>();
        page.setContent(List.of());
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/entries?userId=" + userId + "&page=1&size=5"),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<RestPageResponse<DiaryEntrySummary>>>any()))
            .thenReturn(ResponseEntity.ok(page));

        diaryAPIClient.getEntries(userId, PageRequest.of(1, 5));
    }

    @Test
    public void getInsightsWithTopDistortions() {
        UUID userId = UUID.randomUUID();
        DiaryInsights insights = new DiaryInsights(1, 0.5,
                List.of(new DiaryInsights.TopDistortion("d1", "All-or-nothing", 2)), "s");
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/insights?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(DiaryInsights.class)))
            .thenReturn(ResponseEntity.ok(insights));

        diaryAPIClient.getInsights(userId);
    }

    @Test
    public void getInsightsNullResponse() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
            eq("http://localhost:8080/api/diary/insights?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(DiaryInsights.class)))
            .thenReturn(ResponseEntity.ok(null));

        diaryAPIClient.getInsights(userId);
    }
}
