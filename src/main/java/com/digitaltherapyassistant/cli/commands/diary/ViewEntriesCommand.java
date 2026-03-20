package com.digitaltherapyassistant.cli.commands.diary;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class ViewEntriesCommand implements Command {
    public String getName() { return "b"; }
    public String getMenuLabel() { return "View Entries"; }
    
    public void execute(Scanner in) {
        System.out.println("Viewing Entries...");
    }
}