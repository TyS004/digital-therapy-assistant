package com.digitaltherapyassistant.cli.commands.auth;

import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.auth.AuthAPIClient;


@Component
public class LogoutCommand implements Command {
    private final CLISession session;
    private final AuthAPIClient authApiClient;

    public LogoutCommand(AuthAPIClient authApiClient,
                        CLISession session) {
        this.authApiClient = authApiClient;
        this.session = session;
    }

    public String getName() { return "c"; }
    public String getMenuLabel() { return "Logout"; }
    
    public boolean execute(Scanner in) { 
        authApiClient.logout(session.getToken());
        System.out.println("Logged Out");
        
        return true;
    }
}
