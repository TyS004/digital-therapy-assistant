package com.digitaltherapyassistant.cli.commands.auth;

import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.controller.AuthController;

@Component
public class LogoutCommand implements Command {
    private final AuthController authController;
    public LogoutCommand(AuthController authController) {
        this.authController = authController;
    }

    public String getName() { return "c"; }
    public String getMenuLabel() { return "Logout"; }
    
    public boolean execute(Scanner in) { 
        System.out.println("Logged Out"); 
        return true;
    }
}
