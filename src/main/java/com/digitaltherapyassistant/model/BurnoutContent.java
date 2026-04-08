package com.digitaltherapyassistant.model;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class BurnoutContent extends KnowledgeBaseObject{
    public BurnoutContent(String id, String name, String description, List<String> examples, List<String> reframingQuestions) {
        super(id, name, description, examples, reframingQuestions);
    }
}
