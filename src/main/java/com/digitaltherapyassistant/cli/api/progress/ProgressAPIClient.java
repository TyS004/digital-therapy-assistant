package com.digitaltherapyassistant.cli.api.progress;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public interface ProgressAPIClient {
    void getWeeklySummary(UUID userId);
    void getMonthlyTrends(UUID userId);
    void getAchievements(UUID userId);
}
