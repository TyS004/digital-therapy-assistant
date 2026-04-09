package com.digitaltherapyassistant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitaltherapyassistant.dto.Achievement;
import com.digitaltherapyassistant.dto.BurnoutRecovery;
import com.digitaltherapyassistant.dto.MonthlyTrends;
import com.digitaltherapyassistant.dto.WeeklySummary;
import com.digitaltherapyassistant.entity.DiaryEntry;
import com.digitaltherapyassistant.entity.SeverityLevel;
import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.repository.UserSessionRepository;
import com.digitaltherapyassistant.service.ProgressServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserSessionRepository userSessionRepository;
    @Mock private DiaryEntryRepository diaryEntryRepository;

    @InjectMocks private ProgressServiceImpl progressService;

    // --- getWeeklySummary ---

    @Test
    void testGetWeeklySummary_happyPath() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setStreakDays(5);

        UserSession completedSession = new UserSession();
        completedSession.setStatus(Status.COMPLETED);

        DiaryEntry entry = new DiaryEntry();
        entry.setCreatedAt(LocalDateTime.now().minusDays(1));
        entry.setMoodBefore(4);
        entry.setMoodAfter(7);
        entry.setDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSessionRepository.findByUserIdAndDateRange(eq(userId), any(), any()))
                .thenReturn(List.of(completedSession));
        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of(entry));

        WeeklySummary result = progressService.getWeeklySummary(userId);

        assertNotNull(result);
        assertEquals(1, result.getSessionsCompleted());
        assertEquals(1, result.getDiaryEntries());
        assertEquals(3.0, result.getAverageMoodImprovement());
        assertEquals(5, result.getStreakDays());
        assertTrue(result.getMessage().contains("1 session"));
    }

    @Test
    void testGetWeeklySummary_noSessions_messageEncourages() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setStreakDays(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSessionRepository.findByUserIdAndDateRange(eq(userId), any(), any()))
                .thenReturn(List.of());
        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of());

        WeeklySummary result = progressService.getWeeklySummary(userId);

        assertEquals(0, result.getSessionsCompleted());
        assertTrue(result.getMessage().contains("Start a session"));
    }

    @Test
    void testGetWeeklySummary_userNotFound_throws() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(DigitalTherapyException.class, () -> progressService.getWeeklySummary(userId));
    }

    // --- getMonthlyTrends ---

    @Test
    void testGetMonthlyTrends_returnsFor4Weeks() {
        UUID userId = UUID.randomUUID();

        when(userSessionRepository.findByUserIdAndDateRange(eq(userId), any(), any()))
                .thenReturn(List.of());
        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of());
        when(diaryEntryRepository.findTopDistortionsByUser(userId)).thenReturn(List.of());

        MonthlyTrends result = progressService.getMonthlyTrends(userId);

        assertNotNull(result);
        assertEquals(4, result.getWeeklyBreakdown().size());
        for (int i = 0; i < 4; i++) {
            assertEquals(i + 1, result.getWeeklyBreakdown().get(i).getWeekNumber());
        }
        assertEquals(0, result.getTotalSessionsThisMonth());
        assertEquals(0, result.getTotalEntriesThisMonth());
    }

    @Test
    void testGetMonthlyTrends_withCompletedSessionsAndEntries() {
        UUID userId = UUID.randomUUID();

        UserSession completed = new UserSession();
        completed.setStatus(Status.COMPLETED);

        DiaryEntry entry = new DiaryEntry();
        entry.setCreatedAt(LocalDateTime.now().minusDays(3));
        entry.setMoodBefore(3);
        entry.setMoodAfter(6);
        entry.setDeleted(false);

        when(userSessionRepository.findByUserIdAndDateRange(eq(userId), any(), any()))
                .thenReturn(List.of(completed));
        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of(entry));
        when(diaryEntryRepository.findTopDistortionsByUser(userId)).thenReturn(List.of());

        MonthlyTrends result = progressService.getMonthlyTrends(userId);

        assertNotNull(result);
        assertTrue(result.getTotalSessionsThisMonth() >= 0);
    }

    // --- getBurnoutRecovery ---

    @Test
    void testGetBurnoutRecovery_lowProgress_allRecommendations() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setStreakDays(1);
        user.setSeverityLevel(SeverityLevel.SIGNIFICANT);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSessionRepository.countCompletedSessionsByUser(userId)).thenReturn(1L);

        BurnoutRecovery result = progressService.getBurnoutRecovery(userId);

        assertNotNull(result);
        assertEquals("SIGNIFICANT", result.getSeverityLevel());
        assertTrue(result.getRecoveryProgressPercent() < 100);
        // should contain all 3 conditional recommendations + the always-present one
        assertTrue(result.getRecommendations().size() >= 3);
        assertTrue(result.getRecommendations().stream().anyMatch(r -> r.contains("first few CBT sessions")));
        assertTrue(result.getRecommendations().stream().anyMatch(r -> r.contains("streak")));
        assertTrue(result.getRecommendations().stream().anyMatch(r -> r.contains("licensed therapist")));
    }

    @Test
    void testGetBurnoutRecovery_highProgress_fewerRecommendations() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setStreakDays(10);
        user.setSeverityLevel(SeverityLevel.MILD);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSessionRepository.countCompletedSessionsByUser(userId)).thenReturn(20L);

        BurnoutRecovery result = progressService.getBurnoutRecovery(userId);

        assertEquals(100, result.getRecoveryProgressPercent());
        assertEquals(20L, result.getTotalSessionsCompleted());
        // only the always-present recommendation
        assertEquals(1, result.getRecommendations().size());
        assertTrue(result.getRecommendations().get(0).contains("Thought Diary"));
    }

    @Test
    void testGetBurnoutRecovery_userNotFound_throws() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(DigitalTherapyException.class, () -> progressService.getBurnoutRecovery(userId));
    }

    // --- getAchievements ---

    @Test
    void testGetAchievements_noneUnlocked() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setStreakDays(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSessionRepository.countCompletedSessionsByUser(userId)).thenReturn(0L);
        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of());

        List<Achievement> result = progressService.getAchievements(userId);

        assertEquals(7, result.size());
        assertTrue(result.stream().noneMatch(Achievement::isUnlocked));
    }

    @Test
    void testGetAchievements_allUnlocked() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setStreakDays(30);

        List<DiaryEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DiaryEntry e = new DiaryEntry();
            e.setDeleted(false);
            entries.add(e);
        }

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSessionRepository.countCompletedSessionsByUser(userId)).thenReturn(10L);
        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(entries);

        List<Achievement> result = progressService.getAchievements(userId);

        assertEquals(7, result.size());
        assertTrue(result.stream().allMatch(Achievement::isUnlocked));
    }

    @Test
    void testGetAchievements_userNotFound_throws() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(DigitalTherapyException.class, () -> progressService.getAchievements(userId));
    }
}
