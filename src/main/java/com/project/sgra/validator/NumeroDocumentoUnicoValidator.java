package com.project.sgra.validator;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NumeroDocumentoUnicoValidator implements ConstraintValidator<NumeroDocumentoUnico, Object> {

    private final ConductorRepository conductorRepository;

    public NumeroDocumentoUnicoValidator(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        if (dto instanceof ConductorDTO conductorDTO) {
            return !conductorRepository.existsByNumeroDocumentoAndIdNot(conductorDTO.getNumeroDocumentoDTO(), conductorDTO.getIdDTO());
        }

        if (dto instanceof EditarConductorDTO editarDTO) {
            return !conductorRepository.existsByNumeroDocumentoAndIdNot(editarDTO.getNumeroDocumentoDTO(), editarDTO.getIdDTO());
        }

        return true;
    }

}
