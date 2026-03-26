package com.digitaltherapyassistant.cli.commands.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.controller.AuthController;
import com.digitaltherapyassistant.dto.request.RegisterRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;

import java.util.Scanner;

@Component
public class RegisterCommand implements Command {

    private AuthController authController;

    public RegisterCommand(AuthController authController){
        this.authController = authController;
    }

    public String getName(){ return "a"; }
    public String getMenuLabel(){ return "Register"; }

    public boolean execute(Scanner in){
        System.out.print("Enter a Email: ");
        String userEmail = in.nextLine().trim();
        System.out.print("Enter a Password: ");
        String password = in.nextLine().trim();
        System.out.print("Enter Your Name: ");
        String name = in.nextLine().trim();

        RegisterRequest request = new RegisterRequest(userEmail, password, name);
        ResponseEntity<AuthResponse> response = authController.register(request);

        System.out.println(response.getBody().getMessage());
        return true;
    }
}
