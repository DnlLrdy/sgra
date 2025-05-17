package com.project.sgra.mapper;

import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.model.Conductor;
import org.springframework.stereotype.Component;

@Component
public class EditarConductorMapper {

    public Conductor toEntity(EditarConductorDTO editarConductorDTO) {
        if (editarConductorDTO == null) {
            return null;
        }

        Conductor conductor = new Conductor();
        conductor.setId(editarConductorDTO.getIdDTO());
        conductor.setPrimerNombre(editarConductorDTO.getPrimerNombreDTO());
        conductor.setSegundoNombre(editarConductorDTO.getSegundoNombreDTO());
        conductor.setPrimerApellido(editarConductorDTO.getPrimerApellidoDTO());
        conductor.setSegundoApellido(editarConductorDTO.getSegundoApellidoDTO());
        conductor.setFechaNacimiento(editarConductorDTO.getFechaNacimientoDTO());
        conductor.setTipoDocumento(editarConductorDTO.getTipoDocumentoDTO());
        conductor.setNumeroDocumento(editarConductorDTO.getNumeroDocumentoDTO());
        conductor.setNumeroLicencia(editarConductorDTO.getNumeroLicenciaDTO());
        conductor.setFechaVencimientoLicencia(editarConductorDTO.getFechaVencimientoLicenciaDTO());
        conductor.setCorreoElectronico(editarConductorDTO.getCorreoElectronicoDTO());
        conductor.setTelefono(editarConductorDTO.getTelefonoDTO());
        conductor.setNombreUsuario(editarConductorDTO.getNombreUsuarioDTO());

        return conductor;
    }

    public EditarConductorDTO toDTO(Conductor conductor) {
        if (conductor == null) {
            return null;
        }

        EditarConductorDTO editarConductorDTO = new EditarConductorDTO();
        editarConductorDTO.setIdDTO(conductor.getId());
        editarConductorDTO.setPrimerNombreDTO(conductor.getPrimerNombre());
        editarConductorDTO.setSegundoNombreDTO(conductor.getSegundoNombre());
        editarConductorDTO.setPrimerApellidoDTO(conductor.getPrimerApellido());
        editarConductorDTO.setSegundoApellidoDTO(conductor.getSegundoApellido());
        editarConductorDTO.setFechaNacimientoDTO(conductor.getFechaNacimiento());
        editarConductorDTO.setTipoDocumentoDTO(conductor.getTipoDocumento());
        editarConductorDTO.setNumeroDocumentoDTO(conductor.getNumeroDocumento());
        editarConductorDTO.setNumeroLicenciaDTO(conductor.getNumeroLicencia());
        editarConductorDTO.setFechaVencimientoLicenciaDTO(conductor.getFechaVencimientoLicencia());
        editarConductorDTO.setCorreoElectronicoDTO(conductor.getCorreoElectronico());
        editarConductorDTO.setTelefonoDTO(conductor.getTelefono());
        editarConductorDTO.setNombreUsuarioDTO(conductor.getNombreUsuario());

        return editarConductorDTO;
    }

}
