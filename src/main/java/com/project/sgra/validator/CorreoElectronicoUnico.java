package com.project.sgra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorreoElectronicoUnicoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorreoElectronicoUnico {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
