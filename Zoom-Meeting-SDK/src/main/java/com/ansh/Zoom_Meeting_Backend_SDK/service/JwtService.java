package com.ansh.Zoom_Meeting_Backend_SDK.service;

import com.ansh.Zoom_Meeting_Backend_SDK.dto.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    
    @Value("${zoom.sdk.key}")
    private String sdkKey;
    
    @Value("${zoom.sdk.secret}")
    private String sdkSecret;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public String createJwtToken(AuthRequest authRequest) {
        try {
            // Get current time in seconds (like Math.floor(Date.now() / 1000) in Node.js)
            long currentTimeSeconds = System.currentTimeMillis() / 1000;
            
            // Calculate expiration time exactly like Node.js
            long expirationSeconds;
            if (authRequest.getExpirationSeconds() != null) {
                expirationSeconds = currentTimeSeconds + authRequest.getExpirationSeconds();
            } else {
                // Default to 2 hours (60 * 60 * 2 = 7200 seconds) like Node.js
                expirationSeconds = currentTimeSeconds + (60 * 60 * 2);
            }
            
            // Create JWT header exactly like Node.js KJUR
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            
            // Create JWT payload exactly like Node.js KJUR
            Map<String, Object> payload = new HashMap<>();
            
            // Required fields for Zoom Web SDK (matching Node.js structure exactly)
            payload.put("appKey", sdkKey);  // app key (SDK Key)
            payload.put("sdkKey", sdkKey);  // SDK key
            payload.put("mn", authRequest.getMeetingNumber());  // meeting number
            payload.put("role", authRequest.getRole());  // role
            payload.put("iat", currentTimeSeconds);  // issued at
            payload.put("exp", expirationSeconds);  // expiration time
            payload.put("tokenExp", expirationSeconds);  // token expiration
            payload.put("video_webrtc_mode", authRequest.getVideoWebRtcMode());  // video WebRTC mode
            
            // Convert to JSON strings exactly like Node.js KJUR
            String sHeader = objectMapper.writeValueAsString(header);
            String sPayload = objectMapper.writeValueAsString(payload);
            
            // Create JWT exactly like Node.js KJUR.jws.JWS.sign('HS256', sHeader, sPayload, secret)
            return createJwtManually(sHeader, sPayload, sdkSecret);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }
    
    /**
     * Create JWT manually to match Node.js KJUR.jws.JWS.sign() behavior exactly
     */
    private String createJwtManually(String header, String payload, String secret) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        
        // Base64 URL encode header and payload
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        
        // Create signature input
        String signatureInput = encodedHeader + "." + encodedPayload;
        
        // Create HMAC-SHA256 signature
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        
        byte[] signatureBytes = mac.doFinal(signatureInput.getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        
        // Return JWT: header.payload.signature
        return signatureInput + "." + encodedSignature;
    }
    
    public String getSdkKey() {
        return sdkKey;
    }
} 