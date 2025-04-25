package com.project.sgra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RutaDTO {

    private String id;

    @NotBlank(message = "El nombre de la ruta no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la ruta no puede tener más 100 caracteres.")
    private String nombre;

    @Valid
    private List<ParadaDTO> paradas;

}
