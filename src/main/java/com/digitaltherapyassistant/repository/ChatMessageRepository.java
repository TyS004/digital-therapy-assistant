package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.ChatMessage;
import com.digitaltherapyassistant.entity.UserSession;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    public List<ChatMessage> findAllByUserSession(UserSession session);
}