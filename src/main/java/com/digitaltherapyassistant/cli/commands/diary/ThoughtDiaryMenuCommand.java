package com.digitaltherapyassistant.cli.commands.diary;

import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.MenuHandler;
import com.digitaltherapyassistant.cli.commands.BackCommand;

@Component
public class ThoughtDiaryMenuCommand implements Command {
    private MenuHandler diaryMenuHandler;

    public ThoughtDiaryMenuCommand(
        NewEntryCommand newEntryCommand,
        ViewEntriesCommand viewEntriesCommand,
        ViewInsightsCommand viewInsightsCommand,
        BackCommand backCommand
    ){
        diaryMenuHandler = new MenuHandler(
            List.of(newEntryCommand, viewEntriesCommand,
                viewInsightsCommand, backCommand));
    }

    public String getName() { return "3"; }
    public String getMenuLabel() { return "Thought Diary"; }
    
    public boolean execute(Scanner in) {
        diaryMenuHandler.runMenu("Thought Diary Menu", in);
        return true;
    }
}
