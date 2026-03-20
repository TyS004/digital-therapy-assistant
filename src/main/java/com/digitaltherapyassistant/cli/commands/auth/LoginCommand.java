package com.digitaltherapyassistant.cli.commands.auth;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;

@Component
public class LoginCommand implements Command {
    public LoginCommand() {}

    public String getName() { return "b"; }
    public String getMenuLabel() { return "Login"; }
    
    public void execute(Scanner in) {
        System.out.println("Logged In");
    }
}
