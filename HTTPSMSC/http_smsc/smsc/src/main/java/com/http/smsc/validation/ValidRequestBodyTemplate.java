package com.http.smsc.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequestBodyTemplateValidator.class)
@Documented
public @interface ValidRequestBodyTemplate {
    String message() default "Invalid requestBodyTemplate: must contain text, destination, and dlrwebhookurl fields with correct prefixes according to providerType";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
