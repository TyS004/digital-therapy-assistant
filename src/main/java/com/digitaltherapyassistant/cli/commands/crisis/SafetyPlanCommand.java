package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.crisis.CrisisAPIClient;

@Component
public class SafetyPlanCommand implements Command {
    private final CrisisAPIClient crisisAPIClient;

    public SafetyPlanCommand(CrisisAPIClient crisisAPIClient) {
        this.crisisAPIClient = crisisAPIClient;
    }

    public String getName() { return "c"; }
    public String getMenuLabel() { return "Safety Plan"; }
    
    public boolean execute(Scanner in) {
        UUID userId;
        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch (Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.println("Showing Safety Plan...");
        crisisAPIClient.getSafetyPlan(userId);
        return true;
    }
}