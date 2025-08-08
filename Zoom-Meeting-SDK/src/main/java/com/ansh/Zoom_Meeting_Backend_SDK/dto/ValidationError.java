package com.ansh.Zoom_Meeting_Backend_SDK.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {
    
    @JsonProperty("property")
    private String property;
    
    @JsonProperty("reason")
    private String reason;
    
    // Default constructor
    public ValidationError() {}
    
    // Constructor with all fields
    public ValidationError(String property, String reason) {
        this.property = property;
        this.reason = reason;
    }
    
    // Getters and Setters
    public String getProperty() {
        return property;
    }
    
    public void setProperty(String property) {
        this.property = property;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    @Override
    public String toString() {
        return "ValidationError{" +
                "property='" + property + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
} 