package com.digitaltherapyassistant.model;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CrisisProtocol extends KnowledgeBaseObject {
    public CrisisProtocol(String id, String name, String description, List<String> examples, List<String> reframingQuestions) {
        super(id, name, description, examples, reframingQuestions);
    }
}
