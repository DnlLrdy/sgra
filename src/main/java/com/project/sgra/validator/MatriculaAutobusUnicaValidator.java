package com.project.sgra.validator;

import com.project.sgra.dto.AutobusDTO;
import com.project.sgra.repository.AutobusRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class MatriculaAutobusUnicaValidator implements ConstraintValidator<MatriculaAutobusUnica, AutobusDTO> {

    private final AutobusRepository autobusRepository;

    public MatriculaAutobusUnicaValidator(AutobusRepository autobusRepository) {
        this.autobusRepository = autobusRepository;
    }

    @Override
    public boolean isValid(AutobusDTO autobusDTO, ConstraintValidatorContext context) {
        if (autobusDTO == null || autobusDTO.getMatriculaDTO() == null) return true;

        return !autobusRepository.existsByMatriculaAndIdNot(autobusDTO.getMatriculaDTO(), autobusDTO.getIdDTO());
    }

}
