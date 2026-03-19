package com.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trusted_contact")
@Data
@NoArgsConstructor
public class TrustedContact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user ;

    @Column(name = "name")
    private String name ;

    @Column(name = "phone")
    private String phone ;

    @Column(name = "relationship")
    private String relationship ;
}