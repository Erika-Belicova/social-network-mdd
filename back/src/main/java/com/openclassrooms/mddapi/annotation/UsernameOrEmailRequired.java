package com.openclassrooms.mddapi.annotation;

import com.openclassrooms.mddapi.validator.UsernameOrEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validation annotation to ensure that the annotated DTO
 * contains a non-empty username or email.
 */
@Documented
@Constraint(validatedBy =  UsernameOrEmailValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameOrEmailRequired {

    String message() default "Either username or email is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}