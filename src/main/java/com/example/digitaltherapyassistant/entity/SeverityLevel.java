package com.example.digitaltherapyassistant.entity;

public enum SeverityLevel {
    MILD("Mild"),
    MODERATE("Moderate"),
    SIGNIFICANT("Significant") ;

    private final String displayName ;

    private SeverityLevel(String displayName) {
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