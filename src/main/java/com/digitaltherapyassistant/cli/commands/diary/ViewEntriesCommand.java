package com.digitaltherapyassistant.cli.commands.diary;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.diary.DiaryAPIClient;
import org.springframework.data.domain.Pageable;

@Component
public class ViewEntriesCommand implements Command {
    private final DiaryAPIClient diaryAPIClient;
    public ViewEntriesCommand(DiaryAPIClient diaryAPIClient){
        this.diaryAPIClient = diaryAPIClient;
    }

    public String getName() { return "b"; }
    public String getMenuLabel() { return "View Entries"; }
    
    public boolean execute(Scanner in) {
        UUID userId = null;

        System.out.println("Viewing Entries...");

        System.out.print("Enter User ID: ");
        try { userId = UUID.fromString(in.nextLine().trim()); }
        catch(Exception e) { System.out.println(e.getMessage()); return false; }

        System.out.print("Page number (default 0): ");
        String pageInput = in.nextLine().trim();
        int page;
        try {
            page = pageInput.isBlank() ? 0 : Integer.parseInt(pageInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid page number.");
            return false;
        }

        System.out.print("Page size (default 10): ");
        String sizeInput = in.nextLine().trim();
        int size;
        try {
            size = sizeInput.isBlank() ? 10 : Integer.parseInt(sizeInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid page size.");
            return false;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
  
        diaryAPIClient.getEntries(userId, pageable);
        return true;
    }
}