package com.project.sgra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RutaDTO {

    @NotBlank(message = "Nombre de ruta requerido")
    private String nombre;

    @Valid
    private List<ParadaDTO> paradas;

}
