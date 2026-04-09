package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitaltherapyassistant.cli.api.session.SessionAPIClient;
import com.digitaltherapyassistant.cli.commands.BackCommand;
import com.digitaltherapyassistant.cli.commands.cbt.CBTSessionsMenuCommand;
import com.digitaltherapyassistant.cli.commands.cbt.StartNewSessionCommand;
import com.digitaltherapyassistant.cli.commands.cbt.ViewSessionHistoryCommand;
import com.digitaltherapyassistant.cli.commands.cbt.ViewSessionLibraryCommand;

@ExtendWith(MockitoExtension.class)
public class SessionMenuTest {
    @Mock private SessionAPIClient sessionAPIClient;
    @Mock private Scanner in;

    @Mock StartNewSessionCommand startNewSessionCommandMock;
    @Mock ViewSessionHistoryCommand viewSessionHistoryCommandMock;
    @Mock ViewSessionLibraryCommand viewSessionLibraryCommandMock;
    @Mock BackCommand backCommandMock;

    @InjectMocks StartNewSessionCommand startNewSessionCommand;
    @InjectMocks ViewSessionHistoryCommand viewSessionHistoryCommand;
    @InjectMocks ViewSessionLibraryCommand viewSessionLibraryCommand;

    private CBTSessionsMenuCommand cbtSessionsMenuCommand;

    @BeforeEach
    void setup() {
        startNewSessionCommandMock = new StartNewSessionCommand(sessionAPIClient);
        viewSessionHistoryCommand = new ViewSessionHistoryCommand(sessionAPIClient);
        viewSessionLibraryCommandMock = new ViewSessionLibraryCommand(sessionAPIClient);
        backCommandMock = new BackCommand();

        cbtSessionsMenuCommand = new CBTSessionsMenuCommand(
            startNewSessionCommandMock,
            viewSessionHistoryCommand,
            viewSessionLibraryCommandMock,
            backCommandMock
        );
    }

    @Test
    public void testStartNewSessionCommand(){
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        when(in.nextLine())
        .thenReturn(userId.toString())
        .thenReturn(sessionId.toString());
        assertEquals(true, startNewSessionCommand.execute(in));

        when(in.nextLine())
        .thenReturn("Invalid UUID");
        assertEquals(false, startNewSessionCommand.execute(in));

        when(in.nextLine())
        .thenReturn(userId.toString())
        .thenReturn("Invalid ID");
        assertEquals(false, startNewSessionCommand.execute(in));

        assertEquals("b", startNewSessionCommand.getName());
        assertEquals("Start New Session", startNewSessionCommand.getMenuLabel());
        verify(sessionAPIClient).startSession(userId, sessionId);
    }

    @Test
    public void testViewSessionHistoryCommand(){
        UUID userId = UUID.randomUUID();

        when(in.nextLine())
        .thenReturn(userId.toString())
        .thenReturn("Invalid User ID");

        assertEquals(true, viewSessionHistoryCommand.execute(in));
        assertEquals("c", viewSessionHistoryCommand.getName());
        assertEquals("View Session History", viewSessionHistoryCommand.getMenuLabel());

        assertEquals(false, viewSessionHistoryCommand.execute(in));
        verify(sessionAPIClient).getSessionHistory(userId);
    }

    @Test
    public void testViewSessionLibraryCommand(){
        UUID userId = UUID.randomUUID();

        when(in.nextLine())
        .thenReturn(userId.toString())
        .thenReturn("Invalid User ID");

        assertEquals(true, viewSessionLibraryCommand.execute(in));
        assertEquals("a", viewSessionLibraryCommand.getName());
        assertEquals("View Session Library", viewSessionLibraryCommand.getMenuLabel());

        assertEquals(false, viewSessionLibraryCommand.execute(in));
        verify(sessionAPIClient).getSessionLibrary(userId);
    }

    @Test
    public void testCbtSessionsMenu(){
        when(in.nextLine())
        .thenReturn("0");

        assertEquals(true, cbtSessionsMenuCommand.execute(in));

        assertEquals("CBT Sessions", cbtSessionsMenuCommand.getMenuLabel());
        assertEquals("2", cbtSessionsMenuCommand.getName());
    }
}