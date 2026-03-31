package com.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserSession> userSessions = new ArrayList<>() ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DiaryEntry> diaryEntries = new ArrayList<>() ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TrustedContact> trustedContacts = new ArrayList<>() ;

    @Column(name = "email", unique = true, nullable = false)
    private String email ;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash ;

    @Column(name = "name")
    private String name ;

    @Column(name = "onboarding_complete")
    private Boolean onboardingComplete ;

    // create the enums and map to them ========
    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_path")
    private OnboardingPath onboardingPath = OnboardingPath.SELF ;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level")
    private SeverityLevel severityLevel = SeverityLevel.MILD ;
    // ========

    @Column(name = "streak_days")
    private Integer streakDays ;

    @Column(name = "created_at")
    private LocalDateTime createdAt ;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt ;

    @Column(name = "safety_plan")
    private String safetyPlan ;

}