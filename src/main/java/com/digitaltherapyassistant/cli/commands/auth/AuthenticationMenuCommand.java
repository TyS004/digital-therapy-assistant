package com.digitaltherapyassistant.cli.commands.auth;

import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.MenuHandler;
import com.digitaltherapyassistant.cli.commands.BackCommand;

@Component
public class AuthenticationMenuCommand implements Command {
    private MenuHandler authMenuHandler;

    public AuthenticationMenuCommand(
        RegisterCommand registerCommand,
        LoginCommand loginCommand,
        LogoutCommand logoutCommand,
        BackCommand backCommand
    )
    {
        this.authMenuHandler = new MenuHandler(
            List.of(registerCommand, loginCommand, 
                logoutCommand, backCommand));
    }

    public String getName() { return "1"; }
    public String getMenuLabel() { return "Authentication"; }

    public boolean execute(Scanner in) {
        authMenuHandler.runMenu("Authentication Menu", in);
        return true;
    }
}
