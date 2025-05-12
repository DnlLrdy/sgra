package com.project.sgra.service;

import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.mapper.UsuarioMapper;
import com.project.sgra.model.Usuario;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RegistroService {

    private final UsuarioMapper usuarioMapper;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    public RegistroService(UsuarioMapper usuarioMapper, Validator validator, PasswordEncoder passwordEncoder) {
        this.usuarioMapper = usuarioMapper;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public List<String> validarUsuarioDTO(UsuarioDTO usuarioDTO) {
        List<String> mensajesError = new ArrayList<>();

        Set<ConstraintViolation<UsuarioDTO>> violationsNotBlankAndNotNull = validator.validate(usuarioDTO, UsuarioDTO.NotBlankAndNotNullValidator.class);
        for (ConstraintViolation<UsuarioDTO> violation : violationsNotBlankAndNotNull) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
            for (ConstraintViolation<UsuarioDTO> violation : violations) {
                mensajesError.add(violation.getMessage());
            }
        }

        return mensajesError;
    }

    public Usuario convertirUsuarioDTOAEntidad(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

        return usuario;
    }

}
