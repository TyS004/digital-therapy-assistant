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
    public AuthAPIClientImpl(RestTemplate restTemplate, CLISession session,
        @Value("${cli.api.base-url}") String clientURL){
        super(restTemplate, session, clientURL);
    }

    public void register(RegisterRequest request){
        AuthResponse response = super.POST
            (clientURL + "/api/auth/register", request, AuthResponse.class);

        if(response == null) { return; }
        session.login(clientURL, response.getUserID(), response.getAccessToken());
    }

    public void login(LoginRequest request){
        if(session.isLoggedIn()){
            logger.error("User Already Logged In");
            return;
        }
        
        AuthResponse response = POST
            (clientURL + "/api/auth/login", request, AuthResponse.class);

        if(response == null) { return; }
        session.login(request.getEmail(), response.getUserID(), response.getAccessToken());
    }

    public void refreshToken(String refreshToken){
        AuthResponse response = POST
            (clientURL + "/api/auth/refresh", refreshToken, AuthResponse.class);

        if(response == null){ return; }
        session.setToken(response.getAccessToken());
    }

    public void logout(String accessToken){
        AuthResponse response = new AuthResponse();

        POST(clientURL + "/api/auth/logout", response, AuthResponse.class);
        session.logout();
    }
}
