package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class EmergencyResourcesCommand implements Command {
    public String getName() { return "b"; }
    public String getMenuLabel() { return "Emergency Resources"; }
    
    public void execute(Scanner in) {
        System.out.println("Showing Emergency Resources...");
    }
}