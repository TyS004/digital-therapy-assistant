package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.digitaltherapyassistant.cli.api.diary.DiaryAPIClient;
import com.digitaltherapyassistant.cli.commands.BackCommand;
import com.digitaltherapyassistant.cli.commands.diary.NewEntryCommand;
import com.digitaltherapyassistant.cli.commands.diary.ThoughtDiaryMenuCommand;
import com.digitaltherapyassistant.cli.commands.diary.ViewEntriesCommand;
import com.digitaltherapyassistant.cli.commands.diary.ViewInsightsCommand;

@ExtendWith(MockitoExtension.class)
public class DiaryMenuTest {
    @Mock private DiaryAPIClient diaryAPIClient;
    @Mock private Scanner in;

    private NewEntryCommand newEntryCommand;
    private ViewEntriesCommand viewEntriesCommand;
    private ViewInsightsCommand viewInsightsCommand;
    private ThoughtDiaryMenuCommand thoughtDiaryMenuCommand;

    @BeforeEach
    void setup() {
        newEntryCommand = new NewEntryCommand(diaryAPIClient);
        viewEntriesCommand = new ViewEntriesCommand(diaryAPIClient);
        viewInsightsCommand = new ViewInsightsCommand(diaryAPIClient);
        thoughtDiaryMenuCommand = new ThoughtDiaryMenuCommand(
            newEntryCommand, viewEntriesCommand, viewInsightsCommand, new BackCommand());
    }

    @Test
    public void testNewEntryCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("Work conflict")
            .thenReturn("I always fail")
            .thenReturn("I can improve")
            .thenReturn("5")
            .thenReturn("7")
            .thenReturn("80")
            .thenReturn("40");

        assertEquals(true, newEntryCommand.execute(in));
        verify(diaryAPIClient).createEntry(argThat(id -> id.equals(userId)), any());
        assertEquals("a", newEntryCommand.getName());
        assertEquals("New Entry", newEntryCommand.getMenuLabel());

        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("Work conflict")
            .thenReturn("I always fail")
            .thenReturn("I can improve")
            .thenReturn("")
            .thenReturn("7")
            .thenReturn("80")
            .thenReturn("40");
        assertEquals(false, newEntryCommand.execute(in));

        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("Work conflict")
            .thenReturn("I always fail")
            .thenReturn("I can improve")
            .thenReturn("5")
            .thenReturn("")
            .thenReturn("80")
            .thenReturn("40");
        assertEquals(false, newEntryCommand.execute(in));

        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("Work conflict")
            .thenReturn("I always fail")
            .thenReturn("I can improve")
            .thenReturn("5")
            .thenReturn("7")
            .thenReturn("800")
            .thenReturn("40");
        assertEquals(false, newEntryCommand.execute(in));

        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("Work conflict")
            .thenReturn("I always fail")
            .thenReturn("I can improve")
            .thenReturn("5")
            .thenReturn("7")
            .thenReturn("80")
            .thenReturn("");
        assertEquals(false, newEntryCommand.execute(in));

        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("Work conflict")
            .thenReturn("I always fail")
            .thenReturn("I can improve")
            .thenReturn("5")
            .thenReturn("-4")
            .thenReturn("8")
            .thenReturn("4");
        assertEquals(false, newEntryCommand.execute(in));
    }

    @Test
    public void testViewEntriesCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("")
            .thenReturn("");
        
        assertEquals(true, viewEntriesCommand.execute(in));
        verify(diaryAPIClient).getEntries(argThat(id -> id.equals(userId)), any(Pageable.class));
        assertEquals("b", viewEntriesCommand.getName());
        assertEquals("View Entries", viewEntriesCommand.getMenuLabel());

        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("5")
            .thenReturn("5");
        assertEquals(true, viewEntriesCommand.execute(in));
    }

    @Test
    public void testViewInsightsCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString());

        assertEquals(true, viewInsightsCommand.execute(in));
        verify(diaryAPIClient).getInsights(userId);
        assertEquals("c", viewInsightsCommand.getName());
        assertEquals("View Insights", viewInsightsCommand.getMenuLabel());
    }

    @Test
    public void testThoughtDiaryMenuCommand() {
        when(in.nextLine()).thenReturn("0");
        assertEquals(true, thoughtDiaryMenuCommand.execute(in));
        assertEquals("3", thoughtDiaryMenuCommand.getName());
        assertEquals("Thought Diary", thoughtDiaryMenuCommand.getMenuLabel());
    }

    @Test
    public void newEntryInvalidUserId() {
        when(in.nextLine()).thenReturn("not-uuid");
        assertFalse(newEntryCommand.execute(in));
    }

    @Test
    public void newEntryBlankSituation() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString()).thenReturn("   ");
        assertFalse(newEntryCommand.execute(in));
    }

    @Test
    public void newEntryBlankAutomaticThought() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString()).thenReturn("sit").thenReturn("");
        assertFalse(newEntryCommand.execute(in));
    }

    @Test
    public void newEntrySkipsBlankAlternativeThought() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("sit")
            .thenReturn("thought")
            .thenReturn("")
            .thenReturn("5")
            .thenReturn("6")
            .thenReturn("10")
            .thenReturn("20");
        assertEquals(true, newEntryCommand.execute(in));
        verify(diaryAPIClient).createEntry(argThat(id -> id.equals(userId)), any());
    }

    @Test
    public void newEntryInvalidMoodNumber() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("sit")
            .thenReturn("thought")
            .thenReturn("")
            .thenReturn("x");
        assertFalse(newEntryCommand.execute(in));
    }

    @Test
    public void newEntryMoodOutOfRange() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("sit")
            .thenReturn("thought")
            .thenReturn("")
            .thenReturn("11");
        assertFalse(newEntryCommand.execute(in));
    }

    @Test
    public void newEntryBeliefOutOfRange() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine())
            .thenReturn(userId.toString())
            .thenReturn("sit")
            .thenReturn("thought")
            .thenReturn("")
            .thenReturn("5")
            .thenReturn("5")
            .thenReturn("101");
        assertFalse(newEntryCommand.execute(in));
    }

    @Test
    public void viewEntriesInvalidUserId() {
        when(in.nextLine()).thenReturn("bad");
        assertFalse(viewEntriesCommand.execute(in));
    }

    @Test
    public void viewEntriesInvalidPage() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString()).thenReturn("x");
        assertFalse(viewEntriesCommand.execute(in));
    }

    @Test
    public void viewEntriesInvalidSize() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString()).thenReturn("0").thenReturn("y");
        assertFalse(viewEntriesCommand.execute(in));
    }

    @Test
    public void viewInsightsInvalidUserId() {
        when(in.nextLine()).thenReturn("not-a-uuid");
        assertFalse(viewInsightsCommand.execute(in));
    }
}
