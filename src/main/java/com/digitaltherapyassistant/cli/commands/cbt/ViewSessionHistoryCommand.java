package com.digitaltherapyassistant.cli.commands.cbt;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class ViewSessionHistoryCommand implements Command {
    public String getName() { return "c"; }
    public String getMenuLabel() { return "View Session History"; }
    
    public void execute(Scanner in) {
        System.out.println("Viewing Session History...");
    }
}

