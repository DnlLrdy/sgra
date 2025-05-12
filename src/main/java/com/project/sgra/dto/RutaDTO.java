package com.project.sgra.dto;

import com.project.sgra.validator.NombreRutaUnico;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NombreRutaUnico(message = "{rutaDTO.nombreRutaUnico}")
public class RutaDTO {

    public interface NotBlankValidator {
    }

    @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "{rutaDTO.idDTO.pattern}")
    private String idDTO;

    @NotBlank(message = "{rutaDTO.nombreDTO.notBlank}", groups = NotBlankValidator.class)
    @Size(min = 3, max = 50, message = "{rutaDTO.nombreDTO.size}")
    private String nombreDTO;

    @NotEmpty(message = "{rutaDTO.paradasDTO.notEmpty}")
    private List<ParadaDTO> paradasDTO;

}
