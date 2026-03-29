package com.digitaltherapyassistant.cli.commands;
import java.util.Scanner;

import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class ExitCommand implements Command {

    public ExitCommand() {}

    public String getName(){ return "7"; }
    public String getMenuLabel() { return "Exit"; }

    public boolean execute(Scanner in) {
        System.exit(0);
        return true;
    }
}
