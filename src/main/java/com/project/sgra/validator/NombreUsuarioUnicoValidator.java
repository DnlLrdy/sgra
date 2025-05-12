package com.project.sgra.validator;

import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NombreUsuarioUnicoValidator implements ConstraintValidator<NombreUsuarioUnico, String> {

    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;
    private final AdministradorRepository administradorRepository;

    public NombreUsuarioUnicoValidator(UsuarioRepository usuarioRepository,
                                       ConductorRepository conductorRepository,
                                       AdministradorRepository administradorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.conductorRepository = conductorRepository;
        this.administradorRepository = administradorRepository;
    }

    @Override
    public boolean isValid(String nombreUsuario, ConstraintValidatorContext context) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) return true;

        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) return false;
        if (conductorRepository.existsByNombreUsuario(nombreUsuario)) return false;
        return !administradorRepository.existsByNombreUsuario(nombreUsuario);
    }

}
