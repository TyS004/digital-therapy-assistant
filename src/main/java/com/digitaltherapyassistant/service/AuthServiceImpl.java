package com.digitaltherapyassistant.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer;
import org.springframework.security.config.annotation.web.configurers.saml2.Saml2LogoutConfigurer.LogoutRequestConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.dto.request.LoginRequest;
import com.digitaltherapyassistant.dto.request.RegisterRequest;
import com.digitaltherapyassistant.dto.response.AuthResponse;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, 
                            JwtTokenProvider tokenProvider,
                            PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request){
        AuthResponse response = new AuthResponse();

        if(!userRepository.findByEmail(request.getEmail()).isPresent())
        {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setOnboardingComplete(false);
            user.setName(request.getName());
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);

            response.setAccessToken(tokenProvider.generateAccessToken(user.getName()));
            response.setRefreshToken(tokenProvider.generateAccessToken(user.getName()));
            response.setMessage("Registered");
            response.setUserID(user.getId());

            return response;
        }
        response.setMessage("User Already Exists");
        return response;
    }

    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        AuthResponse response = new AuthResponse();
        response.setAccessToken(tokenProvider.generateAccessToken(request.getUsername()));
        request.setUsername(request.getUsername());
        response.setMessage("Logged In User " + request.getUsername());
        
        return response;
    }

    public AuthResponse refreshToken(String refreshToken){
        AuthResponse response = new AuthResponse();
        response.setMessage("Refreshed Token");
        
        return new AuthResponse();
    }

    public void logout(String accessToken){
        
    }
}
