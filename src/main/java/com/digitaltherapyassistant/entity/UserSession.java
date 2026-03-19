package com.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_session")
@Data
@NoArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "userSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> chatMessages = new ArrayList<>() ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cbt_session_id", nullable = false)
    @JsonIgnore
    private CbtSession cbtSession ;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.IN_PROGRESS ;

    @Column(name = "started_at")
    private LocalDateTime startedAt ;

    @Column(name = "ended_at")
    private LocalDateTime endedAt ;


    @Column(name = "mood_before")
    private Integer moodBefore ; // 1-10 -- limit 1-10 on insert

    @Column(name = "mood_after")
    private Integer moodAfter ; // 1-10 -- limit 1-10 on insert


}