package com.digitaltherapyassistant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.digitaltherapyassistant.dto.request.auth.RegisterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "cli.enabled=false")
@AutoConfigureMockMvc
@Transactional
public class DiaryWorkflowIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ChatClient chatClient;
    @MockBean private ChatClient.Builder chatClientBuilder;
    @MockBean private EmbeddingModel embeddingModel;

    // Register and return a JWT token + userId
    private String[] registerAndGetTokenAndUserId() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Diary User");
        request.setEmail("diary@example.com");
        request.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        String token = body.get("accessToken").asText();
        String userId = body.get("userID").asText();
        return new String[]{token, userId};
    }

    @Test
    void testCreateAndRetrieveDiaryEntry() throws Exception {
        String[] auth = registerAndGetTokenAndUserId();
        String token = auth[0];
        String userId = auth[1];

        String entryJson = """
                {
                  "situation": "Work presentation went badly",
                  "automaticThought": "I always fail",
                  "moodBefore": 3,
                  "moodAfter": 6,
                  "beliefRatingBefore": 80,
                  "beliefRatingAfter": 40
                }
                """;

        // Create entry
        MvcResult createResult = mockMvc.perform(post("/api/diary/entries?userId=" + userId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(entryJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.situation").value("Work presentation went badly"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String entryId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        // Get entries list
        mockMvc.perform(get("/api/diary/entries?userId=" + userId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].situation").value("Work presentation went badly"));

        // Get entry detail
        mockMvc.perform(get("/api/diary/entries/" + entryId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situation").value("Work presentation went badly"));
    }

    @Test
    void testCreateAndDeleteDiaryEntry() throws Exception {
        String[] auth = registerAndGetTokenAndUserId();
        String token = auth[0];
        String userId = auth[1];

        String entryJson = """
                {
                  "situation": "Argument with friend",
                  "automaticThought": "They hate me",
                  "moodBefore": 2,
                  "moodAfter": 5
                }
                """;

        // Create entry
        MvcResult createResult = mockMvc.perform(post("/api/diary/entries?userId=" + userId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(entryJson))
                .andExpect(status().isCreated())
                .andReturn();

        String entryId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        // Delete entry
        mockMvc.perform(delete("/api/diary/entries/" + entryId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Entry should no longer be accessible
        mockMvc.perform(get("/api/diary/entries/" + entryId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testSuggestDistortions() throws Exception {
        String[] auth = registerAndGetTokenAndUserId();
        String token = auth[0];

        String body = "{\"thought\": \"I always fail at everything\"}";

        mockMvc.perform(post("/api/diary/distortions/suggest")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].distortionId").value("all-or-nothing"));
    }
}
