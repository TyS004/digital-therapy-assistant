package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.digitaltherapyassistant.cli.commands.BackCommand;
import com.digitaltherapyassistant.cli.commands.ExitCommand;
import com.digitaltherapyassistant.cli.commands.settings.SettingsCommand;

public class CommonCommandsTest {
    @Test
    public void testBackCommand() {
        BackCommand command = new BackCommand();
        assertEquals("0", command.getName());
        assertEquals("Back", command.getMenuLabel());
        assertEquals(true, command.execute(mock(Scanner.class)));
    }

    @Test
    public void testSettingsCommand() {
        SettingsCommand command = new SettingsCommand();
        assertEquals("6", command.getName());
        assertEquals("Settings", command.getMenuLabel());
        assertEquals(true, command.execute(mock(Scanner.class)));
    }

    @Test
    public void testExitCommand() {
        Runnable exitHandler = mock(Runnable.class);
        ExitCommand command = new ExitCommand(exitHandler);
        assertEquals("7", command.getName());
        assertEquals("Exit", command.getMenuLabel());
        assertEquals(true, command.execute(mock(Scanner.class)));
        verify(exitHandler).run();
    }
}
