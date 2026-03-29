package com.digitaltherapyassistant.cli.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;
import com.digitaltherapyassistant.dto.request.LoginRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;

@Component
public abstract class APIClient {
    protected final RestTemplate restTemplate;
    protected final CLISession session;
    protected static final Logger logger = LoggerFactory.getLogger(APIClient.class);

    public APIClient(RestTemplate restTemplate, CLISession session) {
        this.restTemplate = restTemplate;
        this.session = session;
    }
}
