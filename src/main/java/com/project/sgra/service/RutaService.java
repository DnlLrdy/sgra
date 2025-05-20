package com.project.sgra.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.dto.ParadaDTO;
import com.project.sgra.dto.RutaDTO;
import com.project.sgra.mapper.RutaMapper;
import com.project.sgra.model.Ruta;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RutaService {

    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final RutaMapper rutaMapper;

    public RutaService(ObjectMapper objectMapper, Validator validator, RutaMapper rutaMapper) {
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.rutaMapper = rutaMapper;
    }

    public List<String> validarRutaDTO(RutaDTO rutaDTO, String paradasJson) {
        List<String> mensajesError = new ArrayList<>();

        validarFormatoParadas(paradasJson, rutaDTO, mensajesError);

        validarRestriccionesRutaDTO(rutaDTO, mensajesError);

        validarParadasDTO(rutaDTO, mensajesError);

        return mensajesError;
    }

    private void validarFormatoParadas(String paradasJson, RutaDTO rutaDTO, List<String> mensajesError) {
        try {
            List<ParadaDTO> paradasDTO = Arrays.asList(objectMapper.readValue(paradasJson, ParadaDTO[].class));
            rutaDTO.setParadasDTO(paradasDTO);
        } catch (Exception e) {
            mensajesError.add("Formato invalido de paradas.");
        }
    }

    private void validarRestriccionesRutaDTO(RutaDTO rutaDTO, List<String> mensajesError) {
        Set<ConstraintViolation<RutaDTO>> violations = validator.validate(rutaDTO, RutaDTO.NotBlankValidator.class);
        for (ConstraintViolation<RutaDTO> violation : violations) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<RutaDTO>> violations2 = validator.validate(rutaDTO);
            for (ConstraintViolation<RutaDTO> violation : violations2) {
                mensajesError.add(violation.getMessage());
            }
        }
    }

    private void validarParadasDTO(RutaDTO rutaDTO, List<String> mensajesError) {
        for (int i = 0; i < rutaDTO.getParadasDTO().size(); i++) {
            ParadaDTO parada = rutaDTO.getParadasDTO().get(i);
            Set<ConstraintViolation<ParadaDTO>> paradaViolations = validator.validate(parada);
            for (ConstraintViolation<ParadaDTO> violation : paradaViolations) {
                mensajesError.add("[Parada #" + (i + 1) + "] " + violation.getMessage());
            }
        }
    }

    public Ruta convertirRutaDtoAEntidad(RutaDTO rutaDTO) {
        return rutaMapper.toEntity(rutaDTO);
    }

}
