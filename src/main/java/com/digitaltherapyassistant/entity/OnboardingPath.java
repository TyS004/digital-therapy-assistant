package com.digitaltherapyassistant.entity;

public enum OnboardingPath {
    SELF("Self"),
    THERAPIST_REFERRED("Therapist") ;

    private final String displayName ;

    private OnboardingPath(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}