package com.digitaltherapyassistant.exception;

public class DigitalTherapyException extends RuntimeException {
    private String reason;

    public DigitalTherapyException(String reason) { this.reason = reason; }
    
    public String getReason() { return this.reason; }
    public void setReason(String reason) { this.reason = reason; }
}
