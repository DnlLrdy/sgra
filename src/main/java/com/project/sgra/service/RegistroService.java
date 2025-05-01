package com.project.sgra.service;

import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RegistroService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<String> validarUsuario(UsuarioDTO usuarioDTO, BindingResult bindingResult) {
        List<String> mensajesError = new ArrayList<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);

        for (ConstraintViolation<UsuarioDTO> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), null, violation.getMessage());
        }

        if (!bindingResult.hasFieldErrors("email") && emailEnUso(usuarioDTO.getEmail())) {
            bindingResult.rejectValue("email", null, "El email ya se encuentra en uso.");
        }

        if (!bindingResult.hasFieldErrors("nombreUsuario") && nombreUsuarioEnUso(usuarioDTO.getNombreUsuario())) {
            bindingResult.rejectValue("nombreUsuario", null, "El nombre de usuario ya se encuentra en uso.");
        }

        if (!bindingResult.hasFieldErrors("contraseña") &&
                !bindingResult.hasFieldErrors("confirmarContraseña") &&
                !usuarioDTO.getContraseña().equals(usuarioDTO.getConfirmarContraseña())) {
            bindingResult.rejectValue("confirmarContraseña", null, "Las contraseñas no coinciden.");
        }

        bindingResult.getAllErrors().forEach(error -> mensajesError.add(error.getDefaultMessage()));
        return mensajesError;
    }

    public Usuario convertirDtoAEntidad(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        usuario.setPrimerNombre(usuarioDTO.getPrimerNombre());
        usuario.setSegundoNombre(usuarioDTO.getSegundoNombre());
        usuario.setPrimerApellido(usuarioDTO.getPrimerApellido());
        usuario.setSegundoApellido(usuarioDTO.getSegundoApellido());
        usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());

        String contraseñaEncriptada = passwordEncoder.encode(usuarioDTO.getContraseña());
        usuario.setContraseña(contraseñaEncriptada);

        return usuario;
    }

    private boolean emailEnUso(String email) {
        return usuarioRepository.existsByEmail(email) ||
                conductorRepository.existsByEmail(email) ||
                administradorRepository.existsByEmail(email);
    }

    private boolean nombreUsuarioEnUso(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario) ||
                conductorRepository.existsByNombreUsuario(nombreUsuario) ||
                administradorRepository.existsByNombreUsuario(nombreUsuario);
    }

}
