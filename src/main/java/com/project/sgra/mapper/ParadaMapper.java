package com.project.sgra.mapper;

import com.project.sgra.dto.ParadaDTO;
import com.project.sgra.model.Parada;
import org.springframework.stereotype.Component;

@Component
public class ParadaMapper {

    public Parada toEntity(ParadaDTO paradaDTO) {
        if (paradaDTO == null) {
            return null;
        }

        Parada parada = new Parada();
        parada.setNombre(paradaDTO.getNombreDTO());
        parada.setLatitud(paradaDTO.getLatitudDTO());
        parada.setLongitud(paradaDTO.getLongitudDTO());
        parada.setColor(paradaDTO.getColorDTO());

        return parada;
    }

    public ParadaDTO toDTO(Parada parada) {
        if (parada == null) {
            return null;
        }

        ParadaDTO paradaDTO = new ParadaDTO();
        paradaDTO.setNombreDTO(parada.getNombre());
        paradaDTO.setLatitudDTO(parada.getLatitud());
        paradaDTO.setLongitudDTO(parada.getLongitud());
        paradaDTO.setColorDTO(parada.getColor());

        return paradaDTO;
    }

}
