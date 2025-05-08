package com.project.sgra.service;

import com.project.sgra.dto.AutobusDTO;
import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.model.Autobus;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.AutobusRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AutobusService {

    @Autowired
    private AutobusRepository autobusRepository;

    public List<String> validarAutobus(AutobusDTO autobusDTO, BindingResult bindingResult) {
        List<String> mensajesError = new ArrayList<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AutobusDTO>> violations = validator.validate(autobusDTO);

        for (ConstraintViolation<AutobusDTO> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), null, violation.getMessage());
        }

        Optional<Autobus> autobusExiste = autobusRepository.findByMatricula(autobusDTO.getMatricula());

        if (autobusExiste.isPresent() && !autobusExiste.get().getId().equals(autobusDTO.getId()) && !bindingResult.hasFieldErrors("matricula")) {
            bindingResult.rejectValue("matricula", null, "La existe un autobus con esa matricula.");
        }

        bindingResult.getAllErrors().forEach(error -> mensajesError.add(error.getDefaultMessage()));
        return mensajesError;
    }

    public Autobus convertirDtoAEntidad(AutobusDTO autobusDTO) {
        Autobus autobus = new Autobus();

        autobus.setMatricula(autobusDTO.getMatricula());
        autobus.setModelo(autobusDTO.getModelo());
        autobus.setCapacidad(autobusDTO.getCapacidad());
        autobus.setEstado(autobusDTO.getEstado());

        return autobus;
    }

}
