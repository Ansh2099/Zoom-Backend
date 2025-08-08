package com.ansh.Zoom_Meeting_Backend_SDK.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    
    @JsonProperty("signature")
    private String signature;
    
    @JsonProperty("sdkKey")
    private String sdkKey;
    
    // Default constructor
    public AuthResponse() {}
    
    // Constructor with all fields
    public AuthResponse(String signature, String sdkKey) {
        this.signature = signature;
        this.sdkKey = sdkKey;
    }
    
    // Getters and Setters
    public String getSignature() {
        return signature;
    }
    
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    public String getSdkKey() {
        return sdkKey;
    }
    
    public void setSdkKey(String sdkKey) {
        this.sdkKey = sdkKey;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "signature='" + signature + '\'' +
                ", sdkKey='" + sdkKey + '\'' +
                '}';
    }
} 