package com.digitaltherapyassistant.cli.commands.progress;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class WeeklySummaryCommand implements Command {
    public String getName() { return "a"; }
    public String getMenuLabel() { return "Weekly Summary"; }
    
    public boolean execute(Scanner in) {
        System.out.println("Showing Weekly Summary...");
        return true;
    }
}