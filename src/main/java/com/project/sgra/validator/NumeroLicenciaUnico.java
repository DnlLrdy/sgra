package com.project.sgra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumeroLicenciaUnicoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumeroLicenciaUnico {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
