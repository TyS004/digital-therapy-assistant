package com.digitaltherapyassistant.cli.commands.cbt;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.session.SessionAPIClient;

@Component
public class ViewSessionHistoryCommand implements Command {
    private final SessionAPIClient sessionAPIClient;
    public ViewSessionHistoryCommand(SessionAPIClient sessionAPIClient){
        this.sessionAPIClient = sessionAPIClient;
    }

    public String getName() { return "c"; }
    public String getMenuLabel() { return "View Session History"; }
    
    public boolean execute(Scanner in) {
        UUID userId = null;

        System.out.print("Enter a User ID: ");
        try {userId = UUID.fromString(in.nextLine());}
        catch(Exception e) { System.out.println(e.getMessage()); return false; }

        sessionAPIClient.getSessionHistory(userId);
        
        return true;
    }
}

