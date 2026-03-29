package com.digitaltherapyassistant.cli.api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.cli.api.APIClient;
import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;

@Component
public class AuthAPIClientImpl extends APIClient implements AuthAPIClient{
    private String clientURL;

    public AuthAPIClientImpl(RestTemplate restTemplate, CLISession session,
        @Value("${cli.api.base-url}") String clientURL
    ) {
        super(restTemplate, session);
        this.clientURL = clientURL;
    }

    public AuthResponse register(RegisterRequest request){
        AuthResponse response;
        try{
            response = restTemplate.postForObject(
                this.clientURL + "/api/auth/register",
                 request,
                  AuthResponse.class);
            session.login(request.getEmail(), response.getUserID(), response.getAccessToken());

            return response;
        }
        catch(Exception e){
            response = new AuthResponse();
            response.setMessage("Register Failed: " + e.getMessage());
            return response;
        }
    }

    public AuthResponse login(LoginRequest request){
        AuthResponse response = new AuthResponse();
        if(session.isLoggedIn()){
            response.setMessage("Already Logged In");
            return response;
        }

        try{
            response = restTemplate.postForObject(
                this.clientURL + "/api/auth/login",
                request,
                AuthResponse.class);

            session.login(request.getEmail(), response.getUserID(), response.getAccessToken());

            return response;

        }catch(Exception e){
            response.setMessage("Log In Failed: " + e.getMessage() + "\n Invalid Credentials");
            return response;
        }
    }

    public AuthResponse refreshToken(String refreshToken){
        AuthResponse response = new AuthResponse();
        return response;
    }

    public AuthResponse logout(String accessToken){
        AuthResponse response = new AuthResponse();
        try{
            response = restTemplate.postForObject(
                this.clientURL + "/api/auth/logout",
                accessToken,
                  AuthResponse.class);

            session.logout();
            logger.info("Deleted Session Data");
            return response;
        }catch(Exception e){
            response.setMessage("Log In Failed: " + e.getMessage() + "\n Already logged out");
            return response;
        }
    }
}
