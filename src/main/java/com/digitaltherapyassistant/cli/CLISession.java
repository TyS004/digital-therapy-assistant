package com.digitaltherapyassistant.cli;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CLISession {
    private String token;
    private UUID userId;
    private String email;

    public CLISession() {}

    public boolean isLoggedIn(){
        return this.token != null;
    }

    public void login(String email, UUID userId, String token){
        this.email = email;
        this.userId = userId;
        this.token = token;
    }

    public void logout(){
        this.email = null;
        this.userId = null;
        this.token = null;
    }

    public void setToken(String token) { this.token = token; }
    public void setUserID(UUID userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return this.token; }
    public UUID getUserID() { return this.userId; }
    public String getEmail() { return this.email; }
}
