package com.digitaltherapyassistant.cli.commands.cbt;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.session.SessionAPIClient;

@Component
public class StartNewSessionCommand implements Command {
    private SessionAPIClient sessionAPIClient;

    public StartNewSessionCommand(SessionAPIClient sessionAPIClient){
        this.sessionAPIClient = sessionAPIClient;
    }

    public String getName() { return "b"; }
    public String getMenuLabel() { return "Start New Session"; }
    
    public boolean execute(Scanner in) {
        UUID userId = null;
        UUID sessionId = null;

        System.out.print("Enter a User ID: ");
        try {userId = UUID.fromString(in.nextLine());}
        catch(Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.print("Enter a Session ID: ");
        try {sessionId = UUID.fromString(in.nextLine());}
        catch(Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.println("Starting New Session...");
        sessionAPIClient.startSession(userId, sessionId);

        return true;
    }
}

