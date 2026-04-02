package com.digitaltherapyassistant.cli.commands.cbt;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.session.SessionAPIClient;

@Component
public class ViewSessionLibraryCommand implements Command {
    private SessionAPIClient sessionAPIClient;

    public ViewSessionLibraryCommand(SessionAPIClient sessionAPIClient){
        this.sessionAPIClient = sessionAPIClient;
    }

    public String getName() { return "a"; }
    public String getMenuLabel() { return "View Session Library"; }
    
    public boolean execute(Scanner in) {
        UUID userId = null;

        System.out.print("Enter a User ID: ");
        try {userId = UUID.fromString(in.nextLine());}
        catch(Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.println("Viewing Session Library...");
        sessionAPIClient.getSessionLibrary(userId);

        return true;
    }
}
