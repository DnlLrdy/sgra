package com.project.sgra.validator;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CorreoElectronicoUnicoValidator implements ConstraintValidator<CorreoElectronicoUnico, Object> {

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
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        String correElectronico;
        String id;

        if (dto instanceof UsuarioDTO usuarioDTO) {
            correElectronico = usuarioDTO.getCorreoElectronicoDTO();
            id = usuarioDTO.getIdDTO();
        } else if (dto instanceof ConductorDTO conductorDTO) {
            correElectronico = conductorDTO.getCorreoElectronicoDTO();
            id = conductorDTO.getIdDTO();
        } else if (dto instanceof EditarConductorDTO editarConductorDTO) {
            correElectronico = editarConductorDTO.getCorreoElectronicoDTO();
            id = editarConductorDTO.getIdDTO();
        } else {
            return true;
        }

        if (correElectronico == null || correElectronico.isEmpty()) return true;

        if (usuarioRepository.existsByCorreoElectronicoAndIdNot(correElectronico, id)) return false;
        if (conductorRepository.existsByCorreoElectronicoAndIdNot(correElectronico, id)) return false;

        return !administradorRepository.existsByCorreoElectronico(correElectronico);
    }

}
