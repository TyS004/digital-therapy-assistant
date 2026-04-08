package com.digitaltherapyassistant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.digitaltherapyassistant.dto.request.auth.LoginRequest;
import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.digitaltherapyassistant.dto.response.auth.AuthResponse;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.security.JwtTokenProvider;
import com.digitaltherapyassistant.security.TokenBlackListService;
import com.digitaltherapyassistant.service.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private JwtTokenProvider tokenProvider;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenBlackListService tokenBlackListService;

    @InjectMocks private AuthServiceImpl authServiceImpl;

    @Test
    public void registerTest(){

        //200 OK
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@chapman.edu");
        request.setName("User");
        request.setPassword("password");

        when(userRepository.findByEmail("user@chapman.edu"))
        .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password"))
        .thenReturn("encodedPassword");

        when(tokenProvider.generateAccessToken(any(String.class)))
        .thenReturn("accessToken");
        when(tokenProvider.generateRefreshToken(any(String.class)))
        .thenReturn("refreshToken");
        

        AuthResponse response = authServiceImpl.register(request);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Registered", response.getMessage());

        //400 User Already Exists
        when(userRepository.findByEmail("user@chapman.edu"))
        .thenReturn(Optional.of(new User()));

        assertThrows(DigitalTherapyException.class, () -> authServiceImpl.register(request));
    }

    @Test
    public void testLogin(){
        UUID userId = UUID.randomUUID();
        
        User user = new User();
        user.setEmail("user@chapman.edu");
        user.setPasswordHash("password");
        user.setId(userId);
        user.setName("User");
        
        //200 OK
        LoginRequest request = new LoginRequest();
        request.setEmail("user@chapman.edu");
        request.setPassword("password");

        when(userRepository.findByEmail("user@chapman.edu"))
        .thenReturn(Optional.of(user));

        when(tokenProvider.generateAccessToken(any(String.class)))
        .thenReturn("accessToken");
        when(tokenProvider.generateRefreshToken(any(String.class)))
        .thenReturn("refreshToken");

        AuthResponse response = authServiceImpl.login(request);
        assertEquals(userId, response.getUserID());
        assertEquals("Logged In User " + "user@chapman.edu", response.getMessage());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        
        assertDoesNotThrow(() -> authServiceImpl.login(request));

        //400 Invalid Credentials
        when(authenticationManager.authenticate(any()))
        .thenThrow(BadCredentialsException.class);

        assertThrows(DigitalTherapyException.class,
                 () -> authServiceImpl.login(request));
    }

    @Test
    public void testRefreshToken(){
        UUID userId = UUID.randomUUID();
        String userEmail = "user@chapman.edu";
        
        User user = new User();
        user.setEmail(userEmail);
        user.setPasswordHash("password");
        user.setId(userId);
        user.setName("User");

        //200 OK
        when(tokenProvider.getEmailFromToken("refreshToken"))
        .thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.of(user));
        when(tokenBlackListService.isBlacklisted("refreshToken"))
        .thenReturn(false);
        when(tokenProvider.validateToken("refreshToken"))
        .thenReturn(true);
        when(tokenProvider.generateAccessToken(userEmail))
        .thenReturn("accessToken");

        assertDoesNotThrow(() -> authServiceImpl.refreshToken("refreshToken"));
        AuthResponse response = authServiceImpl.refreshToken("refreshToken");

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Refreshed Token", response.getMessage());
        assertEquals(userId, response.getUserID());

        // 400 Logged Out
        when(tokenBlackListService.isBlacklisted("refreshToken")).thenReturn(true);
        when(tokenProvider.validateToken("refreshToken")).thenReturn(true); 
        assertThrows(DigitalTherapyException.class,
                () -> authServiceImpl.refreshToken("refreshToken"));

        // 400 Invalid Tokens
        when(tokenBlackListService.isBlacklisted("refreshToken")).thenReturn(false);
        when(tokenProvider.validateToken("refreshToken")).thenReturn(false);
        assertThrows(DigitalTherapyException.class,
                () -> authServiceImpl.refreshToken("refreshToken"));
    }

    @Test
    public void testLogout(){
        assertDoesNotThrow(() -> authServiceImpl.logout("refreshToken"));
        verify(tokenBlackListService).blacklist("refreshToken");
    }
}