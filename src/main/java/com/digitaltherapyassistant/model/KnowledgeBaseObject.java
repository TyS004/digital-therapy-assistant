package com.digitaltherapyassistant.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class KnowledgeBaseObject {

    private String id ;
    private String name ;
    private String description ;
    private List<String> examples ;
    private List<String> reframingQuestions ;

    public KnowledgeBaseObject(String id, String name, String description, List<String> examples, List<String> reframingQuestions) {
        this.id = id ;
        this.name = name ;
        this.description = description ;
        this.examples = examples ;
        this.reframingQuestions = reframingQuestions ;
    }
}
