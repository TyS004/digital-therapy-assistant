package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.MenuHandler;
import com.digitaltherapyassistant.cli.commands.BackCommand;

@Component
public class CrisisSupportMenuCommand implements Command {
    private MenuHandler crisisMenuHandler;

    public CrisisSupportMenuCommand(
        CopingStrategiesCommand copingStrategiesCommand,
        EmergencyResourcesCommand emergencyResourcesCommand,
        SafetyPlanCommand safetyPlanCommand,
        BackCommand backCommand
    ){
       crisisMenuHandler = new MenuHandler(
                List.of(copingStrategiesCommand, emergencyResourcesCommand,
                safetyPlanCommand, backCommand));
    }

    public String getName() { return "5"; }
    public String getMenuLabel() { return "Crisis Support"; }
    
    public void execute(Scanner in) {
        crisisMenuHandler.runMenu("Crisis Menu", in);
    }
}
