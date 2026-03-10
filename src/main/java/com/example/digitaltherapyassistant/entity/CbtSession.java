package com.example.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cbt_session")
@Data
@NoArgsConstructor
public class CbtSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_module_id", nullable = false)
    @JsonIgnore
    private SessionModule module ;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "cbt_session_objectives", joinColumns = @JoinColumn(name = "cbt_session_id"))
    @Column(name = "objectives")
    private List<String> objectives = new ArrayList<>() ;

    @ElementCollection(targetClass = Modality.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "cbt_session_modalities", joinColumns = @JoinColumn(name = "cbt_session_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "modalities")
    private List<Modality> modalities = new ArrayList<>() ;

    @Column(name = "order_index")
    private Integer orderIndex ;
}