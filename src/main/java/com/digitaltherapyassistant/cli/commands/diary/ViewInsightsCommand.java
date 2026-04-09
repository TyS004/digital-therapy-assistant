package com.digitaltherapyassistant.cli.commands.diary;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.diary.DiaryAPIClient;

@Component
public class ViewInsightsCommand implements Command {
    private final DiaryAPIClient diaryAPIClient;
    public ViewInsightsCommand(DiaryAPIClient diaryAPIClient){
        this.diaryAPIClient = diaryAPIClient;
    }

    public String getName() { return "c"; }
    public String getMenuLabel() { return "View Insights"; }
    
    public boolean execute(Scanner in) {
        UUID userId = null;

        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch(Exception e ) { System.out.println(e.getMessage()); return false; }

        diaryAPIClient.getInsights(userId);

        return true;
    }
}