package com.digitaltherapyassistant.cli.commands.progress;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.progress.ProgressAPIClient;

@Component
public class AchievementsCommand implements Command {
    private final ProgressAPIClient progressAPIClient;

    public AchievementsCommand(ProgressAPIClient progressAPIClient) {
        this.progressAPIClient = progressAPIClient;
    }

    public String getName() { return "c"; }
    public String getMenuLabel() { return "Achievements"; }
    
    public boolean execute(Scanner in) {
        UUID userId;
        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch (Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.println("Showing Achievements...");
        progressAPIClient.getAchievements(userId);
        return true;
    }
}