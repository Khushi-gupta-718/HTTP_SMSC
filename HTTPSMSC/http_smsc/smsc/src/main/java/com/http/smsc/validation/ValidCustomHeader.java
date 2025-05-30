package com.http.smsc.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCustomHeaderValidator.class)
@Documented
public @interface ValidCustomHeader {
    String message() default "Invalid customHeaders or providerType";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
