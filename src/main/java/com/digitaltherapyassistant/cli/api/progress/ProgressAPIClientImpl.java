package com.digitaltherapyassistant.cli.api.progress;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.cli.api.APIClient;
import com.digitaltherapyassistant.dto.Achievement;
import com.digitaltherapyassistant.dto.MonthlyTrends;
import com.digitaltherapyassistant.dto.WeeklySummary;

@Component
public class ProgressAPIClientImpl extends APIClient implements ProgressAPIClient {
    public ProgressAPIClientImpl(RestTemplate restTemplate, CLISession session,
            @Value("${cli.api.base-url}") String clientURL) {
        super(restTemplate, session, clientURL);
    }

    @Override
    public void getWeeklySummary(UUID userId) {
        WeeklySummary response = GET(clientURL + "/progress/weekly?userId=" + userId, WeeklySummary.class);
        if (response == null) {
            return;
        }

        System.out.println("\n=== Weekly Summary ===");
        System.out.println("Sessions Completed: " + response.getSessionsCompleted());
        System.out.println("Diary Entries:      " + response.getDiaryEntries());
        System.out.println("Mood Improvement:   " + response.getAverageMoodImprovement());
        System.out.println("Streak Days:        " + response.getStreakDays());
        System.out.println("Message:            " + response.getMessage());
    }

    @Override
    public void getMonthlyTrends(UUID userId) {
        MonthlyTrends response = GET(clientURL + "/progress/monthly?userId=" + userId, MonthlyTrends.class);
        if (response == null) {
            return;
        }

        System.out.println("\n=== Monthly Trends ===");
        System.out.println("Overall Mood Improvement: " + response.getOverallMoodImprovement());
        System.out.println("Total Sessions This Month: " + response.getTotalSessionsThisMonth());
        System.out.println("Total Entries This Month:  " + response.getTotalEntriesThisMonth());
        System.out.println("Top Distortions:           " + response.getTopDistortionsThisMonth());
        for (MonthlyTrends.WeeklyStats stats : response.getWeeklyBreakdown()) {
            System.out.println("Week " + stats.getWeekNumber() + " -> sessions=" + stats.getSessionsCompleted()
                    + ", entries=" + stats.getDiaryEntries()
                    + ", moodImprovement=" + stats.getAverageMoodImprovement());
        }
    }

    @Override
    public void getAchievements(UUID userId) {
        List<Achievement> response = GET(
                clientURL + "/progress/achievements?userId=" + userId,
                new ParameterizedTypeReference<List<Achievement>>() {});
        if (response == null) {
            return;
        }

        System.out.println("\n=== Achievements ===");
        for (Achievement achievement : response) {
            System.out.println("[" + (achievement.isUnlocked() ? "Unlocked" : "Locked") + "] "
                    + achievement.getTitle() + " - " + achievement.getDescription());
        }
    }
}
