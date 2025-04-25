package com.project.sgra.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.dto.ParadaDTO;
import com.project.sgra.dto.RutaDTO;
import com.project.sgra.model.Parada;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.RutaRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

@Service
public class RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    public List<String> validarRuta(RutaDTO rutaDTO, String paradasJson, BindingResult bindingResult) {
        List<String> mensajesError = new ArrayList<>();

        Optional<Ruta> rutaExistente = rutaRepository.findByNombre(rutaDTO.getNombre());

        if (rutaExistente.isPresent() && !rutaExistente.get().getId().equals(rutaDTO.getId())) {
            bindingResult.rejectValue("nombre", null, "Ya existe una ruta con ese nombre.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ParadaDTO> paradas = Arrays.asList(mapper.readValue(paradasJson, ParadaDTO[].class));

            rutaDTO.setParadas(paradas);

            if (paradas.isEmpty()) {
                bindingResult.rejectValue("paradas", null, "Debes crear al menos una parada a la ruta.");
            }
        } catch (Exception e) {
            bindingResult.rejectValue("paradas", null, "Debes crear al menos una parada a la ruta.");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RutaDTO>> violations = validator.validate(rutaDTO);

        for (ConstraintViolation<RutaDTO> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), null, violation.getMessage());
        }

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                if (error instanceof FieldError fieldError) {
                    String field = fieldError.getField();
                    String mensaje = fieldError.getDefaultMessage();

                    if (field.matches("paradas\\[\\d+\\]\\..+")) {
                        int index = Integer.parseInt(field.replaceAll("paradas\\[(\\d+)\\]\\..+", "$1"));
                        mensaje = mensaje + " en la parada #" + (index + 1) + ".";
                    }

                    mensajesError.add(mensaje);
                }
            }
        }

        return mensajesError;
    }

    public Ruta convertirDtoAEntidad(RutaDTO rutaDTO) {
        Ruta ruta = new Ruta();
        ruta.setNombre(rutaDTO.getNombre());

        List<Parada> paradas = rutaDTO.getParadas().stream().map(pDTO -> {
            Parada parada = new Parada();
            parada.setNombre(pDTO.getNombre());
            parada.setLatitud(pDTO.getLatitud());
            parada.setLongitud(pDTO.getLongitud());
            parada.setColor(pDTO.getColor());
            return parada;
        }).toList();

        ruta.setParadas(paradas);
        return ruta;
    }

}
