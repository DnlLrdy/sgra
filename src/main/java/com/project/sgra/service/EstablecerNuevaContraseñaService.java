package com.project.sgra.service;

import com.project.sgra.dto.EstablecerNuevaContraseñaDTO;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EstablecerNuevaContraseñaService {

    private final TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;
    private final Validator validator;

    public EstablecerNuevaContraseñaService(TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository, Validator validator) {
        this.tokenRestablecerContraseñaRepository = tokenRestablecerContraseñaRepository;
        this.validator = validator;
    }

    public List<String> validarEstablecerNuevaContraseñaDTO(EstablecerNuevaContraseñaDTO establecerNuevaContraseñaDTO) {
        List<String> mensajesError = new ArrayList<>();

        Set<ConstraintViolation<EstablecerNuevaContraseñaDTO>> violationsNotBlank = validator.validate(establecerNuevaContraseñaDTO, EstablecerNuevaContraseñaDTO.NotBlankValidator.class);
        for (ConstraintViolation<EstablecerNuevaContraseñaDTO> violation : violationsNotBlank) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<EstablecerNuevaContraseñaDTO>> violations = validator.validate(establecerNuevaContraseñaDTO);
            for (ConstraintViolation<EstablecerNuevaContraseñaDTO> violation : violations) {
                mensajesError.add(violation.getMessage());
            }
        }

        return mensajesError;
    }

    public boolean validarToken(String token) {
        Optional<TokenRestablecerContraseña> tokenExiste = tokenRestablecerContraseñaRepository.findByToken(token);

        if (tokenExiste.isEmpty() || tokenExiste.get().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            tokenRestablecerContraseñaRepository.deleteByToken(token);
            return false;
        }

        return true;
    }

}
