package com.project.sgra.validator;

import com.project.sgra.dto.RutaDTO;
import com.project.sgra.repository.RutaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NombreRutaUnicoValidator implements ConstraintValidator<NombreRutaUnico, RutaDTO> {

    private final RutaRepository rutaRepository;

    public NombreRutaUnicoValidator(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @Override
    public boolean isValid(RutaDTO rutaDTO, ConstraintValidatorContext context) {
        if (rutaDTO == null || rutaDTO.getNombreDTO() == null) return true;

        return !rutaRepository.existsByNombreAndIdNot(rutaDTO.getNombreDTO(), rutaDTO.getIdDTO());
    }

}
