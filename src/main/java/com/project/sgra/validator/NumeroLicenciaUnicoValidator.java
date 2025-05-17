package com.project.sgra.validator;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NumeroLicenciaUnicoValidator implements ConstraintValidator<NumeroLicenciaUnico, Object> {

    private final ConductorRepository conductorRepository;

    public NumeroLicenciaUnicoValidator(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        if (dto instanceof ConductorDTO conductorDTO) {
            return !conductorRepository.existsByNumeroLicenciaAndIdNot(
                    conductorDTO.getNumeroLicenciaDTO(), conductorDTO.getIdDTO()
            );
        }

        if (dto instanceof EditarConductorDTO editarDTO) {
            return !conductorRepository.existsByNumeroLicenciaAndIdNot(
                    editarDTO.getNumeroLicenciaDTO(), editarDTO.getIdDTO()
            );
        }

        return true;
    }

}
