package com.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Data
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_session_id", nullable = false)
    @JsonIgnore
    private UserSession userSession ;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;

    @Column(name = "content")
    private String content ;

    @Enumerated(EnumType.STRING)
    @Column(name = "modality")
    private Modality modality = Modality.TEXT ;

    @Column(name = "timestamp")
    private LocalDateTime timestamp ;

}