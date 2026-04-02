package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class CopingStrategiesCommand implements Command {
    public String getName() { return "a"; }
    public String getMenuLabel() { return "Coping Strategies"; }
    
    public boolean execute(Scanner in) {
        System.out.println("Showing Coping Strategies...");
        return true;
    }
}