package com.digitaltherapyassistant.cli.commands.auth;

import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.Command;
import com.digitaltherapyassistant.cli.api.auth.AuthAPIClient;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;

import java.util.Scanner;

@Component
public class RegisterCommand implements Command {

    private AuthAPIClient authAPIClient;

    public RegisterCommand(AuthAPIClient authAPIClient){
        this.authAPIClient = authAPIClient;
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
        authAPIClient.register(request);
        
        return true;
    }
}
