package com.project.sgra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatriculaAutobusUnicaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatriculaAutobusUnica {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
