package com.digitaltherapyassistant.cli;

public class CommandLineException extends Exception{
    private String reason;

    CommandLineException(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}