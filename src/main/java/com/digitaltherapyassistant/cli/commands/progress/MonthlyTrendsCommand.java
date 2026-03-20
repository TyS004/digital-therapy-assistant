package com.digitaltherapyassistant.cli.commands.progress;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class MonthlyTrendsCommand implements Command {
    public String getName() { return "b"; }
    public String getMenuLabel() { return "Monthly Trends"; }
    
    public void execute(Scanner in) {
        System.out.println("Showing Monthly Trends...");
    }
}