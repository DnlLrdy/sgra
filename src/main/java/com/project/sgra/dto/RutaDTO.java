package com.project.sgra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RutaDTO {

    private String id;

    @NotBlank(message = "El nombre de la ruta no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la ruta no puede tener más de 100 caracteres.")
    private String nombre;

    @Valid
    private List<ParadaDTO> paradas;

}
