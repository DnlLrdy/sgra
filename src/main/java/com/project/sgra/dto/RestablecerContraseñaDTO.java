package com.project.sgra.dto;

import com.project.sgra.validator.CorreoElectronicoExiste;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestablecerContraseñaDTO {

    public interface CorreoElectronicoExisteValidator {
    }

    @NotBlank(message = "{restablecerContraseñaDTO.correoElectronicoDTO.notBlank}")
    @Email(message = "{restablecerContraseñaDTO.correoElectronicoDTO.email}")
    @CorreoElectronicoExiste(message = "{restablecerContraseñaDTO.correoElectronicoDTO.correoElectronicoNoExiste}", groups = CorreoElectronicoExisteValidator.class)
    private String correoElectronicoDTO;

}
