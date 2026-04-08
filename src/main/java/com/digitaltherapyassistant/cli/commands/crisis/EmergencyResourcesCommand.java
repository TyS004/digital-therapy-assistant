package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.crisis.CrisisAPIClient;

@Component
public class EmergencyResourcesCommand implements Command {
    private final CrisisAPIClient crisisAPIClient;

    public EmergencyResourcesCommand(CrisisAPIClient crisisAPIClient) {
        this.crisisAPIClient = crisisAPIClient;
    }

    public String getName() { return "b"; }
    public String getMenuLabel() { return "Emergency Resources"; }
    
    public boolean execute(Scanner in) {
        UUID userId;
        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch (Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.println("Showing Emergency Resources...");
        crisisAPIClient.getCrisisHub(userId);
        return true;
    }
}