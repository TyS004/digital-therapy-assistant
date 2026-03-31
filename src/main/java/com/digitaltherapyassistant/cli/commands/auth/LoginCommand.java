package com.digitaltherapyassistant.cli.commands.auth;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.auth.AuthAPIClient;
import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;

@Component
public class LoginCommand implements Command {
    private final AuthAPIClient authApiClient;
    private static final Logger logger = LoggerFactory.getLogger(LoginCommand.class);

    public LoginCommand(AuthAPIClient authApiClient) {
        this.authApiClient = authApiClient;
    }

    public String getName() { return "b"; }
    public String getMenuLabel() { return "Login"; }
    
    public boolean execute(Scanner in) {
        System.out.print("Enter Email: ");
        String email = in.nextLine();
        System.out.print("Enter Password: ");
        String password = in.nextLine();

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        authApiClient.login(request);
        
        return true;
    }
}
