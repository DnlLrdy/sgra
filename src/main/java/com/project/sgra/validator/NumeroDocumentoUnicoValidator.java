package com.project.sgra.validator;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NumeroDocumentoUnicoValidator implements ConstraintValidator<NumeroDocumentoUnico, ConductorDTO> {

    private final ConductorRepository conductorRepository;

    public NumeroDocumentoUnicoValidator(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    @Override
    public boolean isValid(ConductorDTO conductorDTO, ConstraintValidatorContext context) {
        if (conductorDTO == null) return true;

        return !conductorRepository.existsByNumeroDocumentoAndIdNot(conductorDTO.getNumeroDocumentoDTO(), conductorDTO.getIdDTO());
    }

}
