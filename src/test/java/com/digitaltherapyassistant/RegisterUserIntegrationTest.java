package com.digitaltherapyassistant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "cli.enabled=false")
@AutoConfigureMockMvc
@Transactional
public class RegisterUserIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ChatClient chatClient;
    @MockBean private ChatClient.Builder chatClientBuilder;
    @MockBean private EmbeddingModel embeddingModel;

    @Test
    void testRegisterUser_success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Registered"));
    }

    @Test
    void testRegisterUser_duplicateEmail_fails() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("duplicate@example.com");
        request.setPassword("password123");

        // first registration succeeds
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // second registration with same email fails
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterThenLogin_success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Login User");
        registerRequest.setEmail("logintest@example.com");
        registerRequest.setPassword("password123");

        // register
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // login with same credentials
        String loginJson = "{\"email\":\"logintest@example.com\",\"password\":\"password123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Logged In User logintest@example.com"));
    }
}
