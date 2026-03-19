package com.digitaltherapyassistant.cli.commands.auth;

import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;

@Component
public class LogoutCommand implements Command {
    public LogoutCommand() {}

    public String getName() { return "c"; }
    public String getMenuLabel() { return "Logout"; }
    
    public void execute(Scanner in) { 
        System.out.println("Logged Out"); 
    }
}
