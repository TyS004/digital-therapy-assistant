package com.digitaltherapyassistant.cli.commands.settings;

import com.digitaltherapyassistant.cli.Command;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class SettingsCommand implements Command {

    public String getName() { return "6"; }
    public String getMenuLabel() { return "Settings"; }
    
    public boolean execute (Scanner in) {
        System.out.println("Going to Settings...");
        return true;
    }
}
