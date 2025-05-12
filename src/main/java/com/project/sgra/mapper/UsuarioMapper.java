package com.project.sgra.mapper;

import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setPrimerNombre(usuarioDTO.getPrimerNombreDTO());
        usuario.setSegundoNombre(usuarioDTO.getSegundoNombreDTO());
        usuario.setPrimerApellido(usuarioDTO.getPrimerApellidoDTO());
        usuario.setSegundoApellido(usuarioDTO.getSegundoApellidoDTO());
        usuario.setFechaNacimiento(usuarioDTO.getFechaNacimientoDTO());
        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronicoDTO());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuarioDTO());
        usuario.setContraseña(usuarioDTO.getContraseñaDTO());

        return usuario;
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setPrimerNombreDTO(usuario.getPrimerNombre());
        usuarioDTO.setSegundoNombreDTO(usuario.getSegundoNombre());
        usuarioDTO.setPrimerApellidoDTO(usuario.getPrimerApellido());
        usuarioDTO.setSegundoApellidoDTO(usuario.getSegundoApellido());
        usuarioDTO.setFechaNacimientoDTO(usuario.getFechaNacimiento());
        usuarioDTO.setCorreoElectronicoDTO(usuario.getCorreoElectronico());
        usuarioDTO.setNombreUsuarioDTO(usuario.getNombreUsuario());
        usuarioDTO.setContraseñaDTO(usuario.getContraseña());

        return usuarioDTO;
    }

}
