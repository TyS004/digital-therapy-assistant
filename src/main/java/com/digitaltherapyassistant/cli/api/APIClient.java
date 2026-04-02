package com.digitaltherapyassistant.cli.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.digitaltherapyassistant.cli.CLISession;

@Component
public abstract class APIClient {
    protected final RestTemplate restTemplate;
    protected final CLISession session;
    protected final String clientURL;
    
    protected static final Logger logger = LoggerFactory.getLogger(APIClient.class);

    public APIClient(RestTemplate restTemplate, CLISession session, 
            @Value("${cli.api.base-url}") String clientURL) {
        this.restTemplate = restTemplate;
        this.session = session;
        this.clientURL = clientURL;
    }

    public <T> T POST(String url, Object request, Class<T> responseClass){
        HttpEntity<Object> requestEntity = setJWTHeaderWithBody(request);

        try {
            ResponseEntity<T> response = restTemplate.exchange
                (url, HttpMethod.POST, requestEntity, responseClass);
            return response.getBody();
        }
        catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public <T> T GET(String url, Class<T> responseClass){
        HttpEntity<Void> requestEntity = setJWTHeader();

        try { 
            ResponseEntity<T> response = 
                restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseClass);
            return response.getBody();
        }
        catch(Exception e) { 
            logger.error(e.getMessage()); 
            return null;
        }
    }

    public <T> T GET(String url, ParameterizedTypeReference<T> responseType){
        HttpEntity<Void> requestEntity = setJWTHeader();

        try {
            ResponseEntity<T> response = restTemplate.exchange
                (url, HttpMethod.GET, requestEntity, responseType);
            return response.getBody();
        }
        catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private HttpEntity<Void> setJWTHeader(){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        if(session.getToken() != null) {
            header.setBearerAuth(session.getToken());
        }
        return new HttpEntity<>(header);
    }

    private HttpEntity<Object> setJWTHeaderWithBody(Object body){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        if(session.getToken() != null) {
            header.setBearerAuth(session.getToken());
        }
        return new HttpEntity<>(body, header);
    }
}