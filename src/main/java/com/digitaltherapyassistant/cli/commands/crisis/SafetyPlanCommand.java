package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class SafetyPlanCommand implements Command {
    public String getName() { return "c"; }
    public String getMenuLabel() { return "Safety Plan"; }
    
    public void execute(Scanner in) {
        System.out.println("Showing Safety Plan...");
    }
}