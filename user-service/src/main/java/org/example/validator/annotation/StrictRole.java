package org.example.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.validator.clazz.StrictRoleValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE, ElementType.RECORD_COMPONENT, ElementType.FIELD
})
@Constraint(validatedBy = StrictRoleValidator.class)
public @interface StrictRole {
    String message() default "Only accepting 2 roles: ARTIST or USER";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
