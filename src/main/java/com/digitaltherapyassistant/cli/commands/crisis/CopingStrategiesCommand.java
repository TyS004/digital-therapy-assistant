package com.digitaltherapyassistant.cli.commands.crisis;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.crisis.CrisisAPIClient;

@Component
public class CopingStrategiesCommand implements Command {
    private final CrisisAPIClient crisisAPIClient;

    public CopingStrategiesCommand(CrisisAPIClient crisisAPIClient) {
        this.crisisAPIClient = crisisAPIClient;
    }

    public String getName() { return "a"; }
    public String getMenuLabel() { return "Coping Strategies"; }
    
    public boolean execute(Scanner in) {
        System.out.println("Showing Coping Strategies...");
        crisisAPIClient.getCopingStrategies();
        return true;
    }
}