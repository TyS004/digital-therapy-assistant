package com.digitaltherapyassistant.model;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CbtTechnique extends  KnowledgeBaseObject{
    public CbtTechnique(String id, String name, String description, List<String> examples, List<String> reframingQuestions) {
        super(id, name, description, examples, reframingQuestions);
    }
}
