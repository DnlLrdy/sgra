package com.project.sgra.dto;

import com.project.sgra.model.Autobus;
import com.project.sgra.validator.MatriculaAutobusUnica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MatriculaAutobusUnica(message = "{autobusDTO.matriculaAutobusUnica}")
public class AutobusDTO {

    public interface NotBlankAndNotNullValidator {
    }

    @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "{autobusDTO.idDTO.pattern}")
    private String idDTO;

    @NotBlank(message = "{autobusDTO.matriculaDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Pattern(regexp = "^[A-Z]{3}-\\d{3}$", message = "{autobusDTO.matriculaDTO.pattern}")
    private String matriculaDTO;

    @NotBlank(message = "{autobusDTO.modeloDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(max = 50, message = "{autobusDTO.modeloDTO.size}")
    private String modeloDTO;

    @NotNull(message = "{autobusDTO.capacidadDTO.notNull}", groups = NotBlankAndNotNullValidator.class)
    @Positive(message = "{autobusDTO.capacidadDTO.positive}")
    private int capacidadDTO;

    @NotNull(message = "{autobusDTO.estadoDTO.notNull}", groups = NotBlankAndNotNullValidator.class)
    private Autobus.Estado estadoDTO;

}
