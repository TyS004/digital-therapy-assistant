package com.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "diary_entry")
@Data
@NoArgsConstructor
public class DiaryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "situation")
    private String situation;

    @Column(name = "automatic_thought")
    private String automaticThought;

    @ElementCollection(targetClass = EmotionRating.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "diary_entry_emotions", joinColumns = @JoinColumn(name = "diary_entry_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "emotions")
    private List<EmotionRating> emotions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "diary_entry_distortions",
            joinColumns = @JoinColumn(name = "diary_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "cognitive_distortion_id")
    )
    @JsonIgnore
    private List<CognitiveDistortion> distortions;

    @Column(name = "alternative_thought")
    private String alternativeThought;

    @Column(name = "mood_before")
    @Min(1)
    @Max(10)
    private Integer moodBefore;

    @Column(name = "mood_after")
    @Min(1)
    @Max(10)
    private Integer moodAfter;

    @Column(name = "belief_rating_before")
    @Min(1)
    @Max(100)
    private Integer beliefRatingBefore ;

    @Column(name = "belief_rating_after")
    @Min(1)
    @Max(100)
    private Integer beliefRatingAfter ;

    @Column(name = "created_at")
    private LocalDateTime createdAt ;

    @Column(name = "deleted")
    private Boolean deleted ; // a soft delete - placed into traschan - clear trash for full

}