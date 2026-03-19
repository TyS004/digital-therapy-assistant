package com.digitaltherapyassistant.cli.commands.cbt;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;

@Component
public class StartNewSessionCommand implements Command {
    public String getName() { return "b"; }
    public String getMenuLabel() { return "Start New Session"; }
    
    public void execute(Scanner in) {
        System.out.println("Starting New Session...");
    }
}

