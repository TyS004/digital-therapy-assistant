package com.digitaltherapyassistant.cli.commands.diary;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.diary.DiaryAPIClient;
import com.digitaltherapyassistant.dto.DiaryEntryCreate;

@Component
public class NewEntryCommand implements Command {
    private final DiaryAPIClient diaryAPIClient;

    public NewEntryCommand(DiaryAPIClient diaryAPIClient){
        this.diaryAPIClient = diaryAPIClient;
    }

    public String getName() { return "a"; }
    public String getMenuLabel() { return "New Entry"; }
    
    public boolean execute(Scanner in) {
        UUID userId = null;
        DiaryEntryCreate request = new DiaryEntryCreate();

        System.out.println("\n=== New Diary Entry ===");
        
        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch(Exception e ) { System.out.println(e.getMessage()); return false; }

        // Required fields
        System.out.print("Describe your situation: ");
        String situation = in.nextLine();
        if (situation.isBlank()) {
            System.out.println("Situation is required.");
            return false;
        }
        request.setSituation(situation);

        System.out.print("What is your automatic thought? ");
        String automaticThought = in.nextLine();
        if (automaticThought.isBlank()) {
            System.out.println("Automatic thought is required.");
            return false;
        }
        request.setAutomaticThought(automaticThought);

        // Optional fields
        System.out.print("Alternative thought (press Enter to skip): ");
        String alternativeThought = in.nextLine();
        if (!alternativeThought.isBlank()) {
            request.setAlternativeThought(alternativeThought);
        }

        // Mood before
        Integer moodBefore = parseIntInRange("Mood before (1-10): ", 1, 10, in);
        if (moodBefore == null) return false;
        request.setMoodBefore(moodBefore);

        // Mood after
        Integer moodAfter = parseIntInRange("Mood after (1-10): ", 1, 10, in);
        if (moodAfter == null) return false;
        request.setMoodAfter(moodAfter);

        // Belief rating before
        Integer beliefBefore = parseIntInRange("Belief rating before (0-100): ", 0, 100, in);
        if (beliefBefore == null) return false;
        request.setBeliefRatingBefore(beliefBefore);

        // Belief rating after
        Integer beliefAfter = parseIntInRange("Belief rating after (0-100): ", 0, 100, in);
        if (beliefAfter == null) return false;
        request.setBeliefRatingAfter(beliefAfter);

        diaryAPIClient.createEntry(userId, request);
        System.out.println("Diary entry created successfully.");
        return true;
    }

    private Integer parseIntInRange(String prompt, int min, int max, Scanner in) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(in.nextLine().trim());
            if (value < min || value > max) {
                System.out.println("Value must be between " + min + " and " + max + ".");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered.");
            return null;
        }
    }
}