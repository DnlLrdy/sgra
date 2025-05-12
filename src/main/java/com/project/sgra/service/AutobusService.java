package com.project.sgra.service;

import com.project.sgra.dto.AutobusDTO;
import com.project.sgra.mapper.AutobusMapper;
import com.project.sgra.model.Autobus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AutobusService {

    private final Validator validator;
    private final AutobusMapper autobusMapper;

    public AutobusService(Validator validator, AutobusMapper autobusMapper) {
        this.validator = validator;
        this.autobusMapper = autobusMapper;
    }

    public List<String> validarAutobusDTO(AutobusDTO autobusDTO) {
        List<String> mensajesError = new ArrayList<>();

        Set<ConstraintViolation<AutobusDTO>> violationsNotBlankAndNotNull = validator.validate(autobusDTO, AutobusDTO.NotBlankAndNotNullValidator.class);
        for (ConstraintViolation<AutobusDTO> violation : violationsNotBlankAndNotNull) {
            mensajesError.add(violation.getMessage());
        }

        if (mensajesError.isEmpty()) {
            Set<ConstraintViolation<AutobusDTO>> violations = validator.validate(autobusDTO);
            for (ConstraintViolation<AutobusDTO> violation : violations) {
                mensajesError.add(violation.getMessage());
            }
        }

        return mensajesError;
    }

    public Autobus convertirAutobusDTOAEntidad(AutobusDTO autobusDTO) {
        return autobusMapper.toEntity(autobusDTO);
    }

    public void actualizarAutobus(Autobus autobusDB, Autobus autobus) {
        autobusDB.setMatricula(autobus.getMatricula());
        autobusDB.setModelo(autobus.getModelo());
        autobusDB.setCapacidad(autobus.getCapacidad());
        autobusDB.setEstado(autobus.getEstado());
    }

}
