package com.project.sgra.service;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.mapper.ConductorMapper;
import com.project.sgra.mapper.EditarConductorMapper;
import com.project.sgra.model.Autobus;
import com.project.sgra.model.Conductor;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.ConductorRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final AutobusRepository autobusRepository;
    private final ConductorMapper conductorMapper;
    private final EditarConductorMapper editarConductorMapper;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    public ConductorService(ConductorRepository conductorRepository, AutobusRepository autobusRepository, ConductorMapper conductorMapper, EditarConductorMapper editarConductorMapper, Validator validator, PasswordEncoder passwordEncoder) {
        this.conductorRepository = conductorRepository;
        this.autobusRepository = autobusRepository;
        this.conductorMapper = conductorMapper;
        this.editarConductorMapper = editarConductorMapper;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public List<String> validarCoductorDTO(ConductorDTO conductorDTO) {
        List<String> mensajesError = new ArrayList<>();

        Set<ConstraintViolation<ConductorDTO>> violationsNotBlankAndNotNull = validator.validate(conductorDTO, ConductorDTO.NotBlankAndNotNullValidator.class);
        for (ConstraintViolation<ConductorDTO> violation : violationsNotBlankAndNotNull) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<ConductorDTO>> violations = validator.validate(conductorDTO);
            for (ConstraintViolation<ConductorDTO> violation : violations) {
                mensajesError.add(violation.getMessage());
            }
        }

        return mensajesError;
    }

    public List<String> validarEditarCoductorDTO(EditarConductorDTO editarConductorDTO) {
        List<String> mensajesError = new ArrayList<>();

        Set<ConstraintViolation<EditarConductorDTO>> violationsNotBlankAndNotNull = validator.validate(editarConductorDTO, EditarConductorDTO.NotBlankAndNotNullValidator.class);
        for (ConstraintViolation<EditarConductorDTO> violation : violationsNotBlankAndNotNull) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<EditarConductorDTO>> violations = validator.validate(editarConductorDTO);
            for (ConstraintViolation<EditarConductorDTO> violation : violations) {
                mensajesError.add(violation.getMessage());
            }
        }

        return mensajesError;
    }

    public Conductor convertirConductorDTOAEntidad(ConductorDTO conductorDTO) {
        Conductor conductor = conductorMapper.toEntity(conductorDTO);

        conductor.setContraseña(passwordEncoder.encode(conductor.getContraseña()));

        return conductor;
    }

    public Conductor convertirEditarConductorDTOAEntidad(EditarConductorDTO editarConductorDTO) {
        Conductor conductor = editarConductorMapper.toEntity(editarConductorDTO);
        return conductor;
    }

    public void actualizarConductor(Conductor conductorDB, Conductor conductor) {
        conductorDB.setPrimerNombre(conductor.getPrimerNombre());
        conductorDB.setSegundoNombre(conductor.getSegundoNombre());
        conductorDB.setPrimerApellido(conductor.getPrimerApellido());
        conductorDB.setSegundoApellido(conductor.getSegundoApellido());
        conductorDB.setFechaNacimiento(conductor.getFechaNacimiento());
        conductorDB.setTipoDocumento(conductor.getTipoDocumento());
        conductorDB.setNumeroDocumento(conductor.getNumeroDocumento());
        conductorDB.setNumeroLicencia(conductor.getNumeroLicencia());
        conductorDB.setFechaVencimientoLicencia(conductor.getFechaVencimientoLicencia());
        conductorDB.setCorreoElectronico(conductor.getCorreoElectronico());
        conductorDB.setTelefono(conductor.getTelefono());
        conductorDB.setNombreUsuario(conductor.getNombreUsuario());
    }

    public List<Conductor> obtenerConductoresActivosSinAutobus() {
        List<String> idsConductoresConAutobus = autobusRepository.findAll()
                .stream()
                .map(Autobus::getConductor)
                .filter(Objects::nonNull)
                .map(Conductor::getId)
                .distinct()
                .collect(Collectors.toList());

        return conductorRepository.findByEstadoAndIdNotIn(Conductor.Estado.ACTIVO, idsConductoresConAutobus);
    }

}
