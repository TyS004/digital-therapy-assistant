package com.digitaltherapyassistant.cli.commands.auth;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.controller.AuthController;
import com.digitaltherapyassistant.dto.request.LoginRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;

@Component
public class LoginCommand implements Command {
    private final AuthController authController;
    private static final Logger logger = LoggerFactory.getLogger(LoginCommand.class);

    public LoginCommand(AuthController authController) {
        this.authController = authController;
    }

    public String getName() { return "b"; }
    public String getMenuLabel() { return "Login"; }
    
    public boolean execute(Scanner in) {
        System.out.println("Enter Username: ");
        String username = in.nextLine();
        System.out.println("Enter Password: ");
        String password = in.nextLine();

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        ResponseEntity<AuthResponse> httpResponse = authController.login(request);
        logger.info("Returned: " + httpResponse.getStatusCode().toString());
        logger.info(httpResponse.getBody().getMessage());

        return true;
    }
}
