package com.project.sgra.service;

import com.project.sgra.dto.RestablecerContraseñaDTO;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RestablecerContraseñaService {

    private final TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;
    private final Validator validator;

    public RestablecerContraseñaService(TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository, Validator validator) {
        this.tokenRestablecerContraseñaRepository = tokenRestablecerContraseñaRepository;
        this.validator = validator;
    }

    public List<String> validarRestablecerContraseñaDTO(RestablecerContraseñaDTO restablecerContraseñaDTO, String correoElectronico, LocalDateTime fechaExpiracion) {
        List<String> mensajesError = new ArrayList<>();

        Set<ConstraintViolation<RestablecerContraseñaDTO>> violations = validator.validate(restablecerContraseñaDTO);
        for (ConstraintViolation<RestablecerContraseñaDTO> violation : violations) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<RestablecerContraseñaDTO>> violationsCorreoElectronicoExiste = validator.validate(restablecerContraseñaDTO, RestablecerContraseñaDTO.CorreoElectronicoExisteValidator.class);
            for (ConstraintViolation<RestablecerContraseñaDTO> violation : violationsCorreoElectronicoExiste) {
                mensajesError.add(violation.getMessage());
            }
        }

        if (mensajesError.isEmpty()) {
            validarSiExisteToken(correoElectronico, fechaExpiracion, mensajesError);
        }

        return mensajesError;
    }

    public void eliminarTokensInactivos(String correoElectronico, LocalDateTime fechaExpiracion) {
        List<TokenRestablecerContraseña> tokenExiste = tokenRestablecerContraseñaRepository
                .findByCorreoElectronicoAndFechaExpiracionBefore(correoElectronico, fechaExpiracion);

        if (!tokenExiste.isEmpty()) {
            tokenRestablecerContraseñaRepository.deleteAll(tokenExiste);
        }
    }

    public TokenRestablecerContraseña crearTokenRestablecerContraseña(String token, String correoElectronico, LocalDateTime fechaExpiracion) {
        TokenRestablecerContraseña tokenRestablecerContraseña = new TokenRestablecerContraseña();
        tokenRestablecerContraseña.setToken(token);
        tokenRestablecerContraseña.setCorreoElectronico(correoElectronico);
        tokenRestablecerContraseña.setFechaExpiracion(fechaExpiracion.plusMinutes(15));

        return tokenRestablecerContraseña;
    }

    private void validarSiExisteToken(String correoElectronico, LocalDateTime fechaExpiracion, List<String> mensajesError) {
        boolean tokenRestablecerContraseñasDB = tokenRestablecerContraseñaRepository
                .existsByCorreoElectronicoAndFechaExpiracionAfter(correoElectronico, fechaExpiracion);

        if (tokenRestablecerContraseñasDB) {
            mensajesError.add("Ya hemos enviado anteriormente un enlace a tu correo electrónico para restablecer tu contraseña.");
        }
    }

}
