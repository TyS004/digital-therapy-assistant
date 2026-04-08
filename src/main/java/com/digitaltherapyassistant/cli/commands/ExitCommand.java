package com.digitaltherapyassistant.cli.commands;
import java.util.Scanner;

import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class ExitCommand implements Command {
    private final Runnable exitHandler;

    public ExitCommand() {
        this(() -> System.exit(0));
    }

    public ExitCommand(Runnable exitHandler) {
        this.exitHandler = exitHandler;
    }

    public String getName(){ return "7"; }
    public String getMenuLabel() { return "Exit"; }

    public boolean execute(Scanner in) {
        exitHandler.run();
        return true;
    }
}
