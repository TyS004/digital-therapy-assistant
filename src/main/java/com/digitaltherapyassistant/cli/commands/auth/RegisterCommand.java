package com.digitaltherapyassistant.cli.commands.auth;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;

import java.util.Scanner;

@Component
public class RegisterCommand implements Command {

    public String getName(){ return "a"; }
    public String getMenuLabel(){ return "Register"; }

    public void execute(Scanner in){ 
        System.out.println("Registering...");
    }
}
