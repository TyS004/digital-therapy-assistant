package com.digitaltherapyassistant.entity;

public enum Status {
    IN_PROGRESS("IN PROGRESS"),
    COMPLETED("COMPLETED"),
    EARLY_EXIT("EARLY EXIT") ;

    private final String displayName ;

    private Status(String displayName) {
        this.displayName = displayName ;
    }

    public String getDisplayName() {
        return this.displayName ;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}