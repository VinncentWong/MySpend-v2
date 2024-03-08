package org.user.validator.clazz;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.user.validator.annotation.StrictRole;

import static org.user.constant.Constant.ARTIST;
import static org.user.constant.Constant.USER;

public class StrictRoleValidator implements ConstraintValidator<StrictRole, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equals(ARTIST) || s.equals(USER);
    }
}
