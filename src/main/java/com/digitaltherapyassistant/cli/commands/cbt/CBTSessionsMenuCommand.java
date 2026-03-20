package com.digitaltherapyassistant.cli.commands.cbt;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.MenuHandler;
import com.digitaltherapyassistant.cli.commands.BackCommand;

@Component
public class CBTSessionsMenuCommand implements Command {
    private MenuHandler cbtMenuHandler;

    public CBTSessionsMenuCommand(
        StartNewSessionCommand startSessionCommand,
        ViewSessionHistoryCommand viewSessionHistoryCommand,
        ViewSessionLibraryCommand viewSessionLibraryCommand,
        BackCommand backCommand
    ){
        cbtMenuHandler = new MenuHandler(
            List.of(startSessionCommand, 
                viewSessionHistoryCommand, viewSessionLibraryCommand,
                backCommand));
    }
    
    public String getName() { return "2"; }
    public String getMenuLabel() { return "CBT Sessions"; }
    
    public void execute(Scanner in) { 
        cbtMenuHandler.runMenu("CBT Menu", in);
    }
}
