package com.project.sgra.mapper;

import com.project.sgra.dto.AutobusDTO;
import com.project.sgra.model.Autobus;
import org.springframework.stereotype.Component;

@Component
public class AutobusMapper {

    public Autobus toEntity(AutobusDTO autobusDTO) {
        if (autobusDTO == null) {
            return null;
        }

        Autobus autobus = new Autobus();
        autobus.setId(autobusDTO.getIdDTO());
        autobus.setMatricula(autobusDTO.getMatriculaDTO());
        autobus.setModelo(autobusDTO.getModeloDTO());
        autobus.setCapacidad(autobusDTO.getCapacidadDTO());
        autobus.setEstado(autobusDTO.getEstadoDTO());

        return autobus;
    }

    public AutobusDTO toDTO(Autobus autobus) {
        if (autobus == null) {
            return null;
        }

        AutobusDTO autobusDTO = new AutobusDTO();
        autobusDTO.setIdDTO(autobus.getId());
        autobusDTO.setMatriculaDTO(autobus.getMatricula());
        autobusDTO.setModeloDTO(autobus.getModelo());
        autobusDTO.setCapacidadDTO(autobus.getCapacidad());
        autobusDTO.setEstadoDTO(autobus.getEstado());

        return autobusDTO;
    }

}
