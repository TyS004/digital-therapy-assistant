package com.digitaltherapyassistant.cli.commands.diary;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class ViewInsightsCommand implements Command {
    public String getName() { return "c"; }
    public String getMenuLabel() { return "View Insights"; }
    
    public boolean execute(Scanner in) {
        System.out.println("Viewing Insights...");
        return true;
    }
}