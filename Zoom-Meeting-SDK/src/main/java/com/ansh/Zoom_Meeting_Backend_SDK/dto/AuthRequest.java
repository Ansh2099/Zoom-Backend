package com.ansh.Zoom_Meeting_Backend_SDK.dto;

import com.ansh.Zoom_Meeting_Backend_SDK.validation.MeetingNumberAndRoleRequired;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@MeetingNumberAndRoleRequired
public class AuthRequest {
    
    @JsonProperty("meetingNumber")
    private String meetingNumber;
    
    @JsonProperty("role")
    @Min(value = 0, message = "Role must be 0 or 1")
    @Max(value = 1, message = "Role must be 0 or 1")
    private Integer role;
    
    @JsonProperty("expirationSeconds")
    @Min(value = 1800, message = "Expiration seconds must be between 1800 and 172800")
    @Max(value = 172800, message = "Expiration seconds must be between 1800 and 172800")
    private Integer expirationSeconds;
    
    @JsonProperty("videoWebRtcMode")
    @Min(value = 0, message = "Video WebRTC mode must be 0 or 1")
    @Max(value = 1, message = "Video WebRTC mode must be 0 or 1")
    private Integer videoWebRtcMode;
    
    // Default constructor
    public AuthRequest() {}
    
    // Constructor with all fields
    public AuthRequest(String meetingNumber, Integer role, Integer expirationSeconds, Integer videoWebRtcMode) {
        this.meetingNumber = meetingNumber;
        this.role = role;
        this.expirationSeconds = expirationSeconds;
        this.videoWebRtcMode = videoWebRtcMode;
    }
    
    // Getters and Setters
    public String getMeetingNumber() {
        return meetingNumber;
    }
    
    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }
    
    public Integer getRole() {
        return role;
    }
    
    public void setRole(Integer role) {
        this.role = role;
    }
    
    public Integer getExpirationSeconds() {
        return expirationSeconds;
    }
    
    public void setExpirationSeconds(Integer expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
    
    public Integer getVideoWebRtcMode() {
        return videoWebRtcMode;
    }
    
    public void setVideoWebRtcMode(Integer videoWebRtcMode) {
        this.videoWebRtcMode = videoWebRtcMode;
    }
    
    @Override
    public String toString() {
        return "AuthRequest{" +
                "meetingNumber='" + meetingNumber + '\'' +
                ", role=" + role +
                ", expirationSeconds=" + expirationSeconds +
                ", videoWebRtcMode=" + videoWebRtcMode +
                '}';
    }
} 