package com.project.sgra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstablecerNuevaContraseñaDTO {

    @NotBlank(message = "La nueva contraseña no puede estar vacía.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?-])[A-Za-z\\d!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?-]{8,100}$",
            message = "La nueva contraseña debe tener entre 8 y 100 caracteres, incluyendo al menos una letra mayúscula, una letra minúscula, un número y un carácter especial."
    )
    private String nuevaContraseña;

    @NotBlank(message = "El confirmar nueva contraseña no puede estar vacío.")
    private String confirmarNuevaContraseña;

    @NotBlank(message = "El token es obligatorio.")
    private String token;

}
