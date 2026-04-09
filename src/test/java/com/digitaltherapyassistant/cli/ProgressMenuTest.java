package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitaltherapyassistant.cli.api.progress.ProgressAPIClient;
import com.digitaltherapyassistant.cli.commands.BackCommand;
import com.digitaltherapyassistant.cli.commands.progress.AchievementsCommand;
import com.digitaltherapyassistant.cli.commands.progress.MonthlyTrendsCommand;
import com.digitaltherapyassistant.cli.commands.progress.ProgressDashboardMenuCommand;
import com.digitaltherapyassistant.cli.commands.progress.WeeklySummaryCommand;

@ExtendWith(MockitoExtension.class)
public class ProgressMenuTest {
    @Mock private ProgressAPIClient progressAPIClient;
    @Mock private Scanner in;

    private WeeklySummaryCommand weeklySummaryCommand;
    private MonthlyTrendsCommand monthlyTrendsCommand;
    private AchievementsCommand achievementsCommand;
    private ProgressDashboardMenuCommand progressDashboardMenuCommand;

    @BeforeEach
    void setup() {
        weeklySummaryCommand = new WeeklySummaryCommand(progressAPIClient);
        monthlyTrendsCommand = new MonthlyTrendsCommand(progressAPIClient);
        achievementsCommand = new AchievementsCommand(progressAPIClient);
        progressDashboardMenuCommand = new ProgressDashboardMenuCommand(
            weeklySummaryCommand, monthlyTrendsCommand, achievementsCommand, new BackCommand());
    }

    @Test
    public void testWeeklySummaryCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString());
        assertEquals(true, weeklySummaryCommand.execute(in));
        verify(progressAPIClient).getWeeklySummary(userId);
    }

    @Test
    public void testMonthlyTrendsCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString());
        assertEquals(true, monthlyTrendsCommand.execute(in));
        verify(progressAPIClient).getMonthlyTrends(userId);
    }

    @Test
    public void testAchievementsCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString());
        assertEquals(true, achievementsCommand.execute(in));
        verify(progressAPIClient).getAchievements(userId);
    }

    @Test
    public void testProgressDashboardMenuCommand() {
        when(in.nextLine()).thenReturn("0");
        assertEquals(true, progressDashboardMenuCommand.execute(in));
        assertEquals("4", progressDashboardMenuCommand.getName());
        assertEquals("Progress Dashboard", progressDashboardMenuCommand.getMenuLabel());
    }

    @Test
    void weeklySummaryInvalidUserId() {
        when(in.nextLine()).thenReturn("not-uuid");
        assertFalse(weeklySummaryCommand.execute(in));
        verify(progressAPIClient, never()).getWeeklySummary(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void monthlyInvalidUserId() {
        when(in.nextLine()).thenReturn("bad");
        assertFalse(monthlyTrendsCommand.execute(in));
        verify(progressAPIClient, never()).getMonthlyTrends(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void achievementsInvalidUserId() {
        when(in.nextLine()).thenReturn("x");
        assertFalse(achievementsCommand.execute(in));
        verify(progressAPIClient, never()).getAchievements(org.mockito.ArgumentMatchers.any());
    }
}
