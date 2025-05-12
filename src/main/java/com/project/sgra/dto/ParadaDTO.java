package com.project.sgra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.sgra.model.Parada;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParadaDTO {

    @JsonProperty("nombre")
    @NotBlank(message = "{paradaDTO.nombreDTO.notBlank}")
    @Size(min = 3, max = 100, message = "{paradaDTO.nombreDTO.size}")
    private String nombreDTO;

    @JsonProperty("latitud")
    @NotNull(message = "{paradaDTO.latitudDTO.notNull}")
    @DecimalMin(value = "-90.0", message = "{paradaDTO.latitudDTO.decimalMin}")
    @DecimalMax(value = "90.0", message = "{paradaDTO.latitudDTO.decimalMax}")
    private Double latitudDTO;

    @JsonProperty("longitud")
    @NotNull(message = "{paradaDTO.longitudDTO.notNull}")
    @DecimalMin(value = "-180.0", message = "{paradaDTO.longitudDTO.decimalMin}")
    @DecimalMax(value = "180.0", message = "{paradaDTO.longitudDTO.decimalMax}")
    private Double longitudDTO;

    @JsonProperty("color")
    @NotNull(message = "{paradaDTO.colorDTO.notNull}")
    private Parada.Color colorDTO;

}
