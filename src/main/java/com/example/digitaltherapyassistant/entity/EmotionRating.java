package com.example.digitaltherapyassistant.entity;

public enum EmotionRating {
    SAMPLE1("Sample1") ;

    private final String displayName ;

    private EmotionRating(String displayName) {
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