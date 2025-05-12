package com.project.sgra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorreoElectronicoExisteValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorreoElectronicoExiste {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
