package com.digitaltherapyassistant.cli.commands;

import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;

@Component
public class BackCommand implements Command {
    public String getName() { return "0"; }
    public String getMenuLabel() { return "Back"; }
    
    public void execute (Scanner in) {}
}
