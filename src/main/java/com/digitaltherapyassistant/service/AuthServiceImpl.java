package com.digitaltherapyassistant.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.security.JwtTokenProvider;
import com.digitaltherapyassistant.security.TokenBlackListService;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackListService tokenBlackListService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, 
                            JwtTokenProvider tokenProvider,
                            PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager,
                            TokenBlackListService tokenBlackListService) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenBlackListService = tokenBlackListService;
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

            response.setAccessToken(tokenProvider.generateAccessToken(user.getEmail()));
            response.setRefreshToken(tokenProvider.generateRefreshToken(user.getEmail()));
            response.setMessage("Registered");
            response.setUserID(user.getId());

            return response;
        }
        response.setMessage("User Already Exists");
        return response;
    }

    public AuthResponse login(LoginRequest request){
        AuthResponse response = new AuthResponse();

        //Actual Log-In Logic
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                ) 
            );
        }
        catch(BadCredentialsException e){
            response.setMessage("Invalid Credentials");
            return response;
        }

        response.setAccessToken(tokenProvider.generateAccessToken(request.getEmail()));
        response.setRefreshToken(tokenProvider.generateRefreshToken(request.getEmail()));
        response.setMessage("Logged In User " + request.getEmail());
        
        return response;
    }

    public AuthResponse refreshToken(String refreshToken){
        AuthResponse response = new AuthResponse();

        if(tokenBlackListService.isBlacklisted(refreshToken)){
            response.setMessage("User Logged Out");
            return response;
        }
        else if(!tokenProvider.validateToken(refreshToken)){
            response.setMessage("Invalid Token");
            return response;
        }

        String userEmail = tokenProvider.getEmailFromToken(refreshToken);
        //response.setUserID(tokenProvider.getUsernameFromToken());
        response.setAccessToken(tokenProvider.generateAccessToken(userEmail));
        response.setRefreshToken(refreshToken);
        response.setMessage("Refreshed Token");
        
        return response;
    }

    public void logout(String accessToken){
        tokenBlackListService.blacklist(accessToken);
    }
}
