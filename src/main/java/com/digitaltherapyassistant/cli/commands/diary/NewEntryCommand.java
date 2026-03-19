package com.digitaltherapyassistant.cli.commands.diary;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class NewEntryCommand implements Command {
    public String getName() { return "a"; }
    public String getMenuLabel() { return "New Entry"; }
    
    public void execute(Scanner in) {
        System.out.println("Creating New Entry...");
    }
}