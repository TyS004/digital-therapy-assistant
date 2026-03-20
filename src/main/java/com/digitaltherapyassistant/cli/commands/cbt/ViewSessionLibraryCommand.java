package com.digitaltherapyassistant.cli.commands.cbt;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class ViewSessionLibraryCommand implements Command {
    public String getName() { return "a"; }
    public String getMenuLabel() { return "View Session Library"; }
    
    public void execute(Scanner in) {
        System.out.println("Viewing Session Library...");
    }
}
