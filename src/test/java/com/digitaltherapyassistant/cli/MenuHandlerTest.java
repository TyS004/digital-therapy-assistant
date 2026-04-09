package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.digitaltherapyassistant.cli.commands.BackCommand;

class MenuHandlerTest {

    @Test
    void processCommandInvalidThrows() {
        MenuHandler handler = new MenuHandler(List.of(new BackCommand()));
        Scanner in = mock(Scanner.class);
        when(in.nextLine()).thenReturn("not-a-key");
        assertThrows(CommandLineException.class, () -> handler.processCommand("T", in));
    }

    @Test
    void processCommandBackReturnsFalse() throws Exception {
        MenuHandler handler = new MenuHandler(List.of(new BackCommand()));
        Scanner in = mock(Scanner.class);
        when(in.nextLine()).thenReturn("0");
        assertFalse(handler.processCommand("T", in));
    }

    @Test
    void processCommandExecutesCommand() throws Exception {
        Command cmd = mock(Command.class);
        when(cmd.getName()).thenReturn("x");
        when(cmd.getMenuLabel()).thenReturn("Do it");
        MenuHandler handler = new MenuHandler(List.of(cmd));
        Scanner in = mock(Scanner.class);
        when(in.nextLine()).thenReturn("x");
        assertTrue(handler.processCommand("T", in));
        verify(cmd).execute(in);
    }

    @Test
    void runMenuCatchesInvalidThenExitsOnBack() {
        Command cmd = mock(Command.class);
        when(cmd.getName()).thenReturn("a");
        when(cmd.getMenuLabel()).thenReturn("Act");
        Scanner in = mock(Scanner.class);
        when(in.nextLine()).thenReturn("bad", "a", "0");
        MenuHandler handler = new MenuHandler(List.of(cmd, new BackCommand()));
        handler.runMenu("Menu", in);
        verify(cmd).execute(in);
    }

    @Test
    void getInputTrims() {
        MenuHandler handler = new MenuHandler(List.of(new BackCommand()));
        Scanner in = mock(Scanner.class);
        when(in.nextLine()).thenReturn("  z  ");
        assertEquals("z", handler.getInput(in));
    }

    @Test
    void invalidOptionPrints() {
        new MenuHandler(List.of()).invalidOption();
    }

    @Test
    void constructorRejectsNullCommandList() {
        assertThrows(NullPointerException.class, () -> new MenuHandler(null));
    }
}
