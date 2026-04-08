package com.digitaltherapyassistant.cli.api.progress;

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
import com.digitaltherapyassistant.dto.Achievement;
import com.digitaltherapyassistant.dto.MonthlyTrends;
import com.digitaltherapyassistant.dto.MonthlyTrends.WeeklyStats;
import com.digitaltherapyassistant.dto.WeeklySummary;

@ExtendWith(MockitoExtension.class)
public class ProgressAPIClientImplTest {
    @Mock private RestTemplate restTemplate;
    @Mock private CLISession session;

    private ProgressAPIClientImpl progressAPIClient;

    @BeforeEach
    void setup() {
        progressAPIClient = new ProgressAPIClientImpl(restTemplate, session, "http://localhost:8080");
    }

    @Test
    public void testGetWeeklySummary() {
        UUID userId = UUID.randomUUID();
        WeeklySummary weekly = new WeeklySummary(2, 3, 1.5, 5, "Good week");
        when(restTemplate.exchange(
            eq("http://localhost:8080/progress/weekly?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(WeeklySummary.class)))
            .thenReturn(ResponseEntity.ok(weekly));

        progressAPIClient.getWeeklySummary(userId);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/progress/weekly?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(WeeklySummary.class));
    }

    @Test
    public void testGetMonthlyTrends() {
        UUID userId = UUID.randomUUID();
        MonthlyTrends monthly = new MonthlyTrends(List.of(), List.of("D1"), 1.0, 4, 6);
        when(restTemplate.exchange(
            eq("http://localhost:8080/progress/monthly?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(MonthlyTrends.class)))
            .thenReturn(ResponseEntity.ok(monthly));

        progressAPIClient.getMonthlyTrends(userId);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/progress/monthly?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(MonthlyTrends.class));
    }

    @Test
    public void testGetAchievements() {
        UUID userId = UUID.randomUUID();
        List<Achievement> achievements = List.of(new Achievement("id", "title", "desc", true));
        when(restTemplate.exchange(
            eq("http://localhost:8080/progress/achievements?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<Achievement>>>any()))
            .thenReturn(ResponseEntity.ok(achievements));

        progressAPIClient.getAchievements(userId);
        verify(restTemplate).exchange(
            eq("http://localhost:8080/progress/achievements?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<Achievement>>>any());
    }

    @Test
    public void getWeeklySummaryNull() {
        UUID userId = UUID.randomUUID();
        when(restTemplate.exchange(
            eq("http://localhost:8080/progress/weekly?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(WeeklySummary.class)))
            .thenReturn(ResponseEntity.ok(null));

        progressAPIClient.getWeeklySummary(userId);
    }

    @Test
    public void getMonthlyTrendsNullAndWithWeeklyBreakdown() {
        UUID userId = UUID.randomUUID();
        MonthlyTrends withWeeks = new MonthlyTrends(
                List.of(new WeeklyStats(1, 2, 3, 1.2)),
                List.of(),
                0.5, 1, 2);
        when(restTemplate.exchange(
            eq("http://localhost:8080/progress/monthly?userId=" + userId),
            eq(HttpMethod.GET), any(), eq(MonthlyTrends.class)))
            .thenReturn(ResponseEntity.ok(null))
            .thenReturn(ResponseEntity.ok(withWeeks));

        progressAPIClient.getMonthlyTrends(userId);
        progressAPIClient.getMonthlyTrends(userId);
    }

    @Test
    public void getAchievementsNullAndLockedBranch() {
        UUID userId = UUID.randomUUID();
        List<Achievement> achievements = List.of(new Achievement("id", "t", "d", false));
        when(restTemplate.exchange(
            eq("http://localhost:8080/progress/achievements?userId=" + userId),
            eq(HttpMethod.GET), any(), org.mockito.ArgumentMatchers.<ParameterizedTypeReference<List<Achievement>>>any()))
            .thenReturn(ResponseEntity.ok(null))
            .thenReturn(ResponseEntity.ok(achievements));

        progressAPIClient.getAchievements(userId);
        progressAPIClient.getAchievements(userId);
    }
}
