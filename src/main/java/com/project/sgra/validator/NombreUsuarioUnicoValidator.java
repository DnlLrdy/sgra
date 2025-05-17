package com.project.sgra.validator;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NombreUsuarioUnicoValidator implements ConstraintValidator<NombreUsuarioUnico, Object> {

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
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        String nombreUsuario;
        String id;

        if (dto instanceof UsuarioDTO usuarioDTO) {
            nombreUsuario = usuarioDTO.getNombreUsuarioDTO();
            id = usuarioDTO.getIdDTO();
        } else if (dto instanceof ConductorDTO conductorDTO) {
            nombreUsuario = conductorDTO.getNombreUsuarioDTO();
            id = conductorDTO.getIdDTO();
        } else if (dto instanceof EditarConductorDTO editarConductorDTO) {
            nombreUsuario = editarConductorDTO.getNombreUsuarioDTO();
            id = editarConductorDTO.getIdDTO();
        } else {
            return true;
        }

        if (nombreUsuario == null || nombreUsuario.isEmpty()) return true;

        if (usuarioRepository.existsByNombreUsuarioAndIdNot(nombreUsuario, id)) return false;
        if (conductorRepository.existsByNombreUsuarioAndIdNot(nombreUsuario, id)) return false;

        return !administradorRepository.existsByNombreUsuario(nombreUsuario);
    }

}
