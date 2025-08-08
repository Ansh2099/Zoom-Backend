package com.ansh.Zoom_Meeting_Backend_SDK.controller;

import com.ansh.Zoom_Meeting_Backend_SDK.dto.AuthRequest;
import com.ansh.Zoom_Meeting_Backend_SDK.dto.AuthResponse;
import com.ansh.Zoom_Meeting_Backend_SDK.dto.ValidationError;
import com.ansh.Zoom_Meeting_Backend_SDK.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ZoomAuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(ZoomAuthController.class);
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Zoom Meeting Backend SDK");
        response.put("sdkKey", jwtService.getSdkKey());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            logger.info("Received auth request: {}", authRequest);
            
            // Convert string numbers to actual numbers (like Node.js does)
            convertStringNumbersToIntegers(authRequest);
            
            // Validate request manually to match Node.js behavior exactly
            List<ValidationError> validationErrors = validateRequestManually(authRequest);
            if (!validationErrors.isEmpty()) {
                Map<String, List<ValidationError>> errorResponse = new HashMap<>();
                errorResponse.put("errors", validationErrors);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Create JWT token
            String signature = jwtService.createJwtToken(authRequest);
            String sdkKey = jwtService.getSdkKey();
            
            logger.info("Generated JWT token successfully. SDK Key: {}, Token length: {}", sdkKey, signature.length());
            
            AuthResponse response = new AuthResponse(signature, sdkKey);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating JWT token: ", e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }
    
    /**
     * Convert string numbers to actual numbers for role, expirationSeconds, and videoWebRtcMode
     * This replicates the Node.js behavior where string numbers are automatically converted
     */
    private void convertStringNumbersToIntegers(AuthRequest authRequest) {
        // This method is called after validation, so we know the values are valid
        // The conversion is handled by Jackson during deserialization
        // But we can add explicit conversion if needed for edge cases
        logger.debug("Converting string numbers to integers for request: {}", authRequest);
    }
    
    /**
     * Manual validation to match Node.js validation behavior exactly
     */
    private List<ValidationError> validateRequestManually(AuthRequest authRequest) {
        List<ValidationError> errors = new ArrayList<>();
        
        // Validate role (inNumberArray([0, 1]))
        if (authRequest.getRole() != null) {
            if (authRequest.getRole() < 0 || authRequest.getRole() > 1) {
                errors.add(new ValidationError("role", "Value is not valid. Got " + authRequest.getRole() + ", expected [0, 1]"));
            }
        }
        
        // Validate expirationSeconds (isBetween(1800, 172800))
        if (authRequest.getExpirationSeconds() != null) {
            if (authRequest.getExpirationSeconds() < 1800 || authRequest.getExpirationSeconds() > 172800) {
                errors.add(new ValidationError("expirationSeconds", "Value must in between 1800 and 172800"));
            }
        }
        
        // Validate videoWebRtcMode (inNumberArray([0, 1]))
        if (authRequest.getVideoWebRtcMode() != null) {
            if (authRequest.getVideoWebRtcMode() < 0 || authRequest.getVideoWebRtcMode() > 1) {
                errors.add(new ValidationError("videoWebRtcMode", "Value is not valid. Got " + authRequest.getVideoWebRtcMode() + ", expected [0, 1]"));
            }
        }
        
        // Validate meetingNumber and role (isRequiredAllOrNone)
        String meetingNumber = authRequest.getMeetingNumber();
        Integer role = authRequest.getRole();
        
        boolean meetingNumberPresent = meetingNumber != null && !meetingNumber.trim().isEmpty();
        boolean rolePresent = role != null;
        
        if (!((meetingNumberPresent && rolePresent) || (!meetingNumberPresent && !rolePresent))) {
            errors.add(new ValidationError("$schema", "If one of the following properties is present, all or none must be present: meetingNumber, role"));
        }
        
        return errors;
    }
} 