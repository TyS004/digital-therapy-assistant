package com.digitaltherapyassistant.cli.commands.progress;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.progress.ProgressAPIClient;

@Component
public class WeeklySummaryCommand implements Command {
    private final ProgressAPIClient progressAPIClient;

    public WeeklySummaryCommand(ProgressAPIClient progressAPIClient) {
        this.progressAPIClient = progressAPIClient;
    }

    public String getName() { return "a"; }
    public String getMenuLabel() { return "Weekly Summary"; }
    
    public boolean execute(Scanner in) {
        UUID userId;
        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch (Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.println("Showing Weekly Summary...");
        progressAPIClient.getWeeklySummary(userId);
        return true;
    }
}