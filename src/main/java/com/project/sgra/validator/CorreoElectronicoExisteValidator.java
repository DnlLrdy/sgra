package com.project.sgra.validator;

import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CorreoElectronicoExisteValidator implements ConstraintValidator<CorreoElectronicoExiste, String> {

    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;

    public CorreoElectronicoExisteValidator(UsuarioRepository usuarioRepository, ConductorRepository conductorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.conductorRepository = conductorRepository;
    }

    @Override
    public boolean isValid(String correoElectronico, ConstraintValidatorContext context) {
        if (correoElectronico == null || correoElectronico.isEmpty()) return true;

        return usuarioRepository.existsByCorreoElectronico(correoElectronico)
                || conductorRepository.existsByCorreoElectronico(correoElectronico);
    }

}
