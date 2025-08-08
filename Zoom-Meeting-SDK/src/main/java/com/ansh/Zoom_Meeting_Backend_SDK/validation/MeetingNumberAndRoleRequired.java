package com.ansh.Zoom_Meeting_Backend_SDK.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MeetingNumberAndRoleRequiredValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MeetingNumberAndRoleRequired {
    String message() default "Both meetingNumber and role must be provided together, or neither should be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 