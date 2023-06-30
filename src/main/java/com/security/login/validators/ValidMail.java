package com.security.login.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, PARAMETER, RECORD_COMPONENT })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidMail {

    String message() default "Invalid Email";

    Class<?>[] groups() default{};

    Class<? extends  Payload>[] payload() default {};
}
