package com.digitaltherapyassistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="coping_strategy")
@Data
@NoArgsConstructor
public class CopingStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @Column(name="strategy")
    private String strategy ;

    @Column(name="description")
    private String description ;
}
