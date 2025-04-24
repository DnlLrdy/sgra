package com.project.sgra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParadaDTO {

    @NotBlank(message = "El nombre de la parada es requerido")
    private String nombre;

    @NotNull(message = "Latitud requerida")
    private Double latitud;

    @NotNull(message = "Longitud requerida")
    private Double longitud;

    @NotBlank(message = "Color requerido")
    private String color;

}
