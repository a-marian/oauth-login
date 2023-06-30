package com.security.login.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidMail, String> {
    private static final String EMAIL_PATTERN =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.compile(EMAIL_PATTERN).matcher(value).matches();
    }
}
