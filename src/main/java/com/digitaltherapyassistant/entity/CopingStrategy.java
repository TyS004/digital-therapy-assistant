package com.digitaltherapyassistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "coping_strategy")
@Data
@NoArgsConstructor
public class CopingStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "description")
    private String description ;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "coping_strategy_steps", joinColumns = @JoinColumn(name = "coping_strategy_steps_id"))
    @Column(name = "steps")
    private List<String> steps ;
}
