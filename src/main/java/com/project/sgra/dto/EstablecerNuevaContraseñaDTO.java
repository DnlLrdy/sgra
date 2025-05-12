package com.project.sgra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstablecerNuevaContraseñaDTO {

    public interface NotBlankValidator {
    }

    @NotBlank(message = "{establecerNuevaContraseñaDTO.tokenDTO.notBlank}", groups = NotBlankValidator.class)
    private String tokenDTO;

    @NotBlank(message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.notBlank}", groups = NotBlankValidator.class)
    @Size(min = 8, max = 64, message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.size}")
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.pattern.uppercase}"
    )
    @Pattern(
            regexp = ".*[a-z].*",
            message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.pattern.lowercase}"
    )
    @Pattern(
            regexp = ".*\\d.*",
            message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.pattern.digit}"
    )
    @Pattern(
            regexp = ".*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?-].*",
            message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.pattern.special}"
    )
    @Pattern(
            regexp = "^\\S+$",
            message = "{establecerNuevaContraseñaDTO.nuevaContraseñaDTO.pattern.noSpaces}"
    )
    private String nuevaContraseñaDTO;

    @NotBlank(message = "{establecerNuevaContraseñaDTO.confirmarNuevaContraseñaDTO.notBlank}", groups = NotBlankValidator.class)
    private String confirmarNuevaContraseñaDTO;

    @AssertTrue(message = "{establecerNuevaContraseñaDTO.isNuevasContraseñasCoincidenDTO.assertTrue}")
    public boolean isNuevasContraseñasCoincidenDTO() {
        return nuevaContraseñaDTO != null && nuevaContraseñaDTO.equals(confirmarNuevaContraseñaDTO);
    }

}
