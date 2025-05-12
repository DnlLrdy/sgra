package com.project.sgra.mapper;

import com.project.sgra.dto.RutaDTO;
import com.project.sgra.model.Ruta;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RutaMapper {

    public Ruta toEntity(RutaDTO rutaDTO) {
        if (rutaDTO == null) {
            return null;
        }

        Ruta ruta = new Ruta();
        ruta.setId(rutaDTO.getIdDTO());
        ruta.setNombre(rutaDTO.getNombreDTO());
        ruta.setParadas(rutaDTO.getParadasDTO().stream()
                .map(paradaDTO -> new ParadaMapper().toEntity(paradaDTO))
                .collect(Collectors.toList()));

        return ruta;
    }

    public RutaDTO toDTO(Ruta ruta) {
        if (ruta == null) {
            return null;
        }

        RutaDTO rutaDTO = new RutaDTO();
        rutaDTO.setIdDTO(ruta.getId());
        rutaDTO.setNombreDTO(ruta.getNombre());
        rutaDTO.setParadasDTO(ruta.getParadas().stream()
                .map(parada -> new ParadaMapper().toDTO(parada))
                .collect(Collectors.toList()));

        return rutaDTO;
    }

}
