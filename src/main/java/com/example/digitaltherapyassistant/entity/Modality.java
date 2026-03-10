package com.example.digitaltherapyassistant.entity;

public enum Modality {
    TEXT("Text"),
    VOICE("Voice"),
    VIDEO("Video") ;

    private final String displayName ;

    private Modality(String displayName) {
        this.displayName = displayName ;
    }

    public String getDisplayName() {
        return this.displayName ;
    }

    @Override
    public String toString() {
        return this.displayName ;
    }
}