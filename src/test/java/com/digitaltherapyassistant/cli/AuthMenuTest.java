package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitaltherapyassistant.cli.api.auth.AuthAPIClient;
import com.digitaltherapyassistant.cli.commands.BackCommand;
import com.digitaltherapyassistant.cli.commands.auth.AuthenticationMenuCommand;
import com.digitaltherapyassistant.cli.commands.auth.LoginCommand;
import com.digitaltherapyassistant.cli.commands.auth.LogoutCommand;
import com.digitaltherapyassistant.cli.commands.auth.RegisterCommand;

@ExtendWith(MockitoExtension.class)
public class AuthMenuTest {
    @Mock private AuthAPIClient authAPIClient;
    @Mock private Scanner in;
    @Mock CLISession session;

    private RegisterCommand registerCommandMock;
    private LoginCommand loginCommandMock;
    private LogoutCommand logoutCommandMock;
    private BackCommand backCommandMock;

    @InjectMocks RegisterCommand registerCommand;
    @InjectMocks LoginCommand loginCommand;
    @InjectMocks LogoutCommand logoutCommand;
    
    AuthenticationMenuCommand authenticationMenuCommand;

    @BeforeEach
    void setup() {
        loginCommandMock = new LoginCommand(authAPIClient);
        registerCommandMock = new RegisterCommand(authAPIClient);
        logoutCommandMock = new LogoutCommand(authAPIClient, session);
        backCommandMock = new BackCommand();

        authenticationMenuCommand = new AuthenticationMenuCommand(
            registerCommandMock,
            loginCommandMock,
            logoutCommandMock,
            backCommandMock
        );
    }

    @Test
    public void testAuthMenu(){
        when(in.nextLine())
        .thenReturn("0")
        .thenReturn("7");

        assertEquals(true, authenticationMenuCommand.execute(in));

        assertEquals("Authentication", authenticationMenuCommand.getMenuLabel());
        assertEquals("1", authenticationMenuCommand.getName());
    }

    @Test
    public void testRegisterCommand(){
        String email = "user@chapman.edu";
        String password = "password";
        String name = "name";

        when(in.nextLine())
        .thenReturn(email)
        .thenReturn(password)
        .thenReturn(name);

        assertEquals(true, registerCommand.execute(in));
        verify(authAPIClient).register(argThat(req -> 
                req.getEmail().equals(email) &&
                req.getPassword().equals(password) &&
                req.getName().equals(name)
        ));

        assertEquals("Register", registerCommand.getMenuLabel());
        assertEquals("a", registerCommand.getName());
    }

    @Test
    public void testLoginCommand(){
        String email = "user@chapman.edu";
        String password = "password";

        when(in.nextLine())
        .thenReturn(email)
        .thenReturn(password);

        assertEquals(true, loginCommand.execute(in));
        verify(authAPIClient).login(argThat(req ->
            req.getEmail().equals(email) &&
            req.getPassword().equals(password)
        ));

        assertEquals("Login", loginCommand.getMenuLabel());
        assertEquals("b", loginCommand.getName());
    }

    @Test
    public void testLogoutCommand(){
        String token = "token";

        when(session.getToken())
        .thenReturn(token);

        assertEquals(true, logoutCommand.execute(in));
        verify(authAPIClient).logout(argThat(req -> 
            req.equals(session.getToken())
        ));

        assertEquals("Logout", logoutCommand.getMenuLabel());
        assertEquals("c", logoutCommand.getName());
    }
}
