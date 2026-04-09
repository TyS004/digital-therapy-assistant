package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.Achievement;
import com.digitaltherapyassistant.dto.BurnoutRecovery;
import com.digitaltherapyassistant.dto.MonthlyTrends;
import com.digitaltherapyassistant.dto.WeeklySummary;
import com.digitaltherapyassistant.entity.CognitiveDistortion;
import com.digitaltherapyassistant.entity.DiaryEntry;
import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    @Override
    @Transactional(readOnly = true)
    public WeeklySummary getWeeklySummary(UUID userId) {
        User user = getUser(userId);

        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        LocalDateTime now = LocalDateTime.now();

        List<UserSession> weekSessions = userSessionRepository.findByUserIdAndDateRange(userId, weekStart, now);
        long sessionsCompleted = weekSessions.stream()
                .filter(s -> s.getStatus() == Status.COMPLETED)
                .count();

        List<DiaryEntry> weekEntries = diaryEntryRepository.findByUserIdAndDeletedFalse(userId).stream()
                .filter(e -> e.getCreatedAt() != null && e.getCreatedAt().isAfter(weekStart))
                .toList();

        double avgMood = weekEntries.stream()
                .filter(e -> e.getMoodBefore() != null && e.getMoodAfter() != null)
                .mapToInt(e -> e.getMoodAfter() - e.getMoodBefore())
                .average()
                .orElse(0.0);

        int streak = user.getStreakDays() != null ? user.getStreakDays() : 0;

        String message = sessionsCompleted == 0
                ? "Start a session this week to build your streak!"
                : String.format("Great work! You completed %d session(s) this week.", sessionsCompleted);

        return new WeeklySummary((int) sessionsCompleted, weekEntries.size(), avgMood, streak, message);
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyTrends getMonthlyTrends(UUID userId) {
        LocalDateTime monthStart = LocalDateTime.now().minusDays(28);
        LocalDateTime now = LocalDateTime.now();

        List<DiaryEntry> allEntries = diaryEntryRepository.findByUserIdAndDeletedFalse(userId);

        List<MonthlyTrends.WeeklyStats> breakdown = new ArrayList<>();
        for (int week = 0; week < 4; week++) {
            LocalDateTime start = monthStart.plusDays((long) week * 7);
            LocalDateTime end = start.plusDays(7);

            List<UserSession> wSessions = userSessionRepository.findByUserIdAndDateRange(userId, start, end);
            long wCompleted = wSessions.stream().filter(s -> s.getStatus() == Status.COMPLETED).count();

            List<DiaryEntry> wEntries = allEntries.stream()
                    .filter(e -> e.getCreatedAt() != null
                            && e.getCreatedAt().isAfter(start)
                            && e.getCreatedAt().isBefore(end))
                    .toList();

            double wAvgMood = wEntries.stream()
                    .filter(e -> e.getMoodBefore() != null && e.getMoodAfter() != null)
                    .mapToInt(e -> e.getMoodAfter() - e.getMoodBefore())
                    .average()
                    .orElse(0.0);

            breakdown.add(new MonthlyTrends.WeeklyStats(week + 1, (int) wCompleted, wEntries.size(), wAvgMood));
        }

        List<DiaryEntry> monthEntries = allEntries.stream()
                .filter(e -> e.getCreatedAt() != null && e.getCreatedAt().isAfter(monthStart))
                .toList();

        double overallMood = monthEntries.stream()
                .filter(e -> e.getMoodBefore() != null && e.getMoodAfter() != null)
                .mapToInt(e -> e.getMoodAfter() - e.getMoodBefore())
                .average()
                .orElse(0.0);

        List<UserSession> monthSessions = userSessionRepository.findByUserIdAndDateRange(userId, monthStart, now);
        long totalSessions = monthSessions.stream().filter(s -> s.getStatus() == Status.COMPLETED).count();

        List<Object[]> topRaw = diaryEntryRepository.findTopDistortionsByUser(userId);
        List<String> topDistortions = topRaw.stream()
                .limit(3)
                .map(row -> ((CognitiveDistortion) row[0]).getName())
                .toList();

        return new MonthlyTrends(breakdown, topDistortions, overallMood, (int) totalSessions, monthEntries.size());
    }

    @Override
    @Transactional(readOnly = true)
    public BurnoutRecovery getBurnoutRecovery(UUID userId) {
        User user = getUser(userId);

        long totalCompleted = userSessionRepository.countCompletedSessionsByUser(userId);
        int streak = user.getStreakDays() != null ? user.getStreakDays() : 0;
        String severity = user.getSeverityLevel() != null ? user.getSeverityLevel().name() : "MILD";

        int progressPercent = (int) Math.min(100, (totalCompleted * 100) / 20);

        List<String> recommendations = buildRecommendations(severity, streak, totalCompleted);

        return new BurnoutRecovery(severity, progressPercent, totalCompleted, streak, recommendations);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Achievement> getAchievements(UUID userId) {
        User user = getUser(userId);
        long sessionsCompleted = userSessionRepository.countCompletedSessionsByUser(userId);
        List<DiaryEntry> entries = diaryEntryRepository.findByUserIdAndDeletedFalse(userId);
        int streak = user.getStreakDays() != null ? user.getStreakDays() : 0;
        return Achievement.allAchievements(sessionsCompleted, entries.size(), streak);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DigitalTherapyException("User not found: " + userId));
    }

    private List<String> buildRecommendations(String severity, int streak, long completed) {
        List<String> recs = new ArrayList<>();
        if (completed < 3) {
            recs.add("Complete your first few CBT sessions to build a foundation.");
        }
        if (streak < 3) {
            recs.add("Try to log in daily to maintain your streak and build consistency.");
        }
        if ("SIGNIFICANT".equals(severity)) {
            recs.add("Consider speaking with a licensed therapist to complement your progress here.");
        }
        recs.add("Keep using the Thought Diary to track cognitive patterns over time.");
        return recs;
    }
}
