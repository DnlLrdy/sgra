package com.project.sgra.mapper;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.model.Conductor;
import org.springframework.stereotype.Component;

@Component
public class ConductorMapper {

    public Conductor toEntity(ConductorDTO conductorDTO) {
        if (conductorDTO == null) {
            return null;
        }

        Conductor conductor = new Conductor();
        conductor.setId(conductorDTO.getIdDTO());
        conductor.setPrimerNombre(conductorDTO.getPrimerNombreDTO());
        conductor.setSegundoNombre(conductorDTO.getSegundoNombreDTO());
        conductor.setPrimerApellido(conductorDTO.getPrimerApellidoDTO());
        conductor.setSegundoApellido(conductorDTO.getSegundoApellidoDTO());
        conductor.setFechaNacimiento(conductorDTO.getFechaNacimientoDTO());
        conductor.setTipoDocumento(conductorDTO.getTipoDocumentoDTO());
        conductor.setNumeroDocumento(conductorDTO.getNumeroDocumentoDTO());
        conductor.setNumeroLicencia(conductorDTO.getNumeroLicenciaDTO());
        conductor.setFechaVencimientoLicencia(conductorDTO.getFechaVencimientoLicenciaDTO());
        conductor.setCorreoElectronico(conductorDTO.getCorreoElectronicoDTO());
        conductor.setTelefono(conductorDTO.getTelefonoDTO());
        conductor.setNombreUsuario(conductorDTO.getNombreUsuarioDTO());
        conductor.setContraseña(conductorDTO.getContraseñaDTO());

        return conductor;
    }

    public ConductorDTO toDTO(Conductor conductor) {
        if (conductor == null) {
            return null;
        }

        ConductorDTO conductorDTO = new ConductorDTO();
        conductorDTO.setIdDTO(conductor.getId());
        conductorDTO.setPrimerNombreDTO(conductor.getPrimerNombre());
        conductorDTO.setSegundoNombreDTO(conductor.getSegundoNombre());
        conductorDTO.setPrimerApellidoDTO(conductor.getPrimerApellido());
        conductorDTO.setSegundoApellidoDTO(conductor.getSegundoApellido());
        conductorDTO.setFechaNacimientoDTO(conductor.getFechaNacimiento());
        conductorDTO.setTipoDocumentoDTO(conductor.getTipoDocumento());
        conductorDTO.setNumeroDocumentoDTO(conductor.getNumeroDocumento());
        conductorDTO.setNumeroLicenciaDTO(conductor.getNumeroLicencia());
        conductorDTO.setFechaVencimientoLicenciaDTO(conductor.getFechaVencimientoLicencia());
        conductorDTO.setCorreoElectronicoDTO(conductor.getCorreoElectronico());
        conductorDTO.setTelefonoDTO(conductor.getTelefono());
        conductorDTO.setNombreUsuarioDTO(conductor.getNombreUsuario());
        conductorDTO.setContraseñaDTO(conductor.getContraseña());
        conductorDTO.setConfirmarContraseñaDTO(conductor.getContraseña());

        return conductorDTO;
    }

}
