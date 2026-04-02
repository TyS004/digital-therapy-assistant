package com.digitaltherapyassistant.dto.response.session;

import com.digitaltherapyassistant.entity.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private ChatMessage chatMessage;
    private String message;
}
