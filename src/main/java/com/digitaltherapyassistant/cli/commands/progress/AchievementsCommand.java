package com.digitaltherapyassistant.cli.commands.progress;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class AchievementsCommand implements Command {
    public String getName() { return "c"; }
    public String getMenuLabel() { return "Achievements"; }
    
    public void execute(Scanner in) {
        System.out.println("Showing Achievements...");
    }
}