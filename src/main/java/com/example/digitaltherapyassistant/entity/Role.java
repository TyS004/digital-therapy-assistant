package com.example.digitaltherapyassistant.entity;

public enum Role {
    USER("User"),
    ASSISTANT("Assistant") ;

    private final String displayName ;

    private Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName ;
    }

    @Override
    public String toString() {
        return this.displayName ;
    }

}