package com.digitaltherapyassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session_module")
@Data
@NoArgsConstructor
public class SessionModule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CbtSession> cbtSessions = new ArrayList<>() ;

}
