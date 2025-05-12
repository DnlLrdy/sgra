package com.project.sgra.validator;

import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CorreoElectronicoUnicoValidator implements ConstraintValidator<CorreoElectronicoUnico, String> {

    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;
    private final AdministradorRepository administradorRepository;

    public CorreoElectronicoUnicoValidator(UsuarioRepository usuarioRepository,
                                           ConductorRepository conductorRepository,
                                           AdministradorRepository administradorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.conductorRepository = conductorRepository;
        this.administradorRepository = administradorRepository;
    }

    @Override
    public boolean isValid(String correoElectronico, ConstraintValidatorContext context) {
        if (correoElectronico == null || correoElectronico.isEmpty()) return true;

        if (usuarioRepository.existsByCorreoElectronico(correoElectronico)) return false;
        if (conductorRepository.existsByCorreoElectronico(correoElectronico)) return false;
        return !administradorRepository.existsByCorreoElectronico(correoElectronico);
    }

}
