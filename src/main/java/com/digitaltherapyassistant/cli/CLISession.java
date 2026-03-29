package com.digitaltherapyassistant.cli;

import org.springframework.stereotype.Component;

@Component
public class CLISession {
    private String token;
    private String userId;
    private String email;

    public CLISession() {}

    public boolean isLoggedIn(){
        return this.token != null;
    }

    public void login(String email, String userId, String token){
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
    public void setUserID(String userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return this.token; }
    public String getUserID() { return this.userId; }
    public String getEmail() { return this.email; }
}
