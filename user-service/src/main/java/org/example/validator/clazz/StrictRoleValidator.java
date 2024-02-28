package org.example.validator.clazz;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.validator.annotation.StrictRole;

import static org.example.constant.Constant.ARTIST;
import static org.example.constant.Constant.USER;

public class StrictRoleValidator implements ConstraintValidator<StrictRole, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equals(ARTIST) || s.equals(USER);
    }
}
