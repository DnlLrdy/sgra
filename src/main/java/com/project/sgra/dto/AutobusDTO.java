package com.project.sgra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutobusDTO {

    private String id;

    @NotBlank(message = "La matricula del autobus no puede estar vacía.")
    @Size(max = 10, message = "La matricula del autobuse no puede tener más de 10 caracteres.")
    private String matricula;

    @NotBlank(message = "El modelo del autobus no puede estar vacía.")
    @Size(max = 50, message = "El modelo del autobuse no puede tener más de 50 caracteres.")
    private String modelo;

    @NotNull(message = "La capacidad es obligatoria.")
    @Positive(message = "La capacidad debe ser un número positivo.")
    private int capacidad;

    @NotBlank(message = "El estado debe ser obligatorio.")
    private String estado;

}
