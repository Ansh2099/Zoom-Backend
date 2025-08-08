package com.ansh.Zoom_Meeting_Backend_SDK.validation;

import com.ansh.Zoom_Meeting_Backend_SDK.dto.AuthRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MeetingNumberAndRoleRequiredValidator implements ConstraintValidator<MeetingNumberAndRoleRequired, AuthRequest> {
    
    @Override
    public void initialize(MeetingNumberAndRoleRequired constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(AuthRequest authRequest, ConstraintValidatorContext context) {
        if (authRequest == null) {
            return true; // Let @NotNull handle null validation
        }
        
        String meetingNumber = authRequest.getMeetingNumber();
        Integer role = authRequest.getRole();
        
        // Match Node.js logic: check if values are "undefined" (null or empty string)
        // Node.js: Object.keys(body).filter((x) => typeof body[x] !== 'undefined')
        boolean meetingNumberPresent = meetingNumber != null && !meetingNumber.trim().isEmpty();
        boolean rolePresent = role != null;
        
        // Node.js logic: requiredKeys.every((x) => presentKeys.includes(x)) || requiredKeys.every((x) => !presentKeys.includes(x))
        boolean allPresent = meetingNumberPresent && rolePresent;
        boolean allAbsent = !meetingNumberPresent && !rolePresent;
        
        return allPresent || allAbsent;
    }
} 