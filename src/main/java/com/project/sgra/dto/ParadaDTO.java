package com.project.sgra.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ParadaDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    private String nombre;

    @NotNull(message = "La latitud es obligatoria.")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria.")
    private Double longitud;

    @NotBlank(message = "El color es obligatorio.")
    private String color;

}
