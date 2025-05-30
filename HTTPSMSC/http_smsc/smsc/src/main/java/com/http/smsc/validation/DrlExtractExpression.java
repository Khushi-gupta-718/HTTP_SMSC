package com.http.smsc.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateDlrExtractExpressionContent.class)
@Documented
public @interface DrlExtractExpression {
    String message() default "Invalid dlrExtractExpression: must contain status, date, and msgId fields with correct prefixes according to providerType";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
