package com.project.sgra.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioDTO {

    @NotBlank(message = "El primer nombre no puede estar vacío.")
    @Size(max = 50, message = "El primer nombre no puede tener más de 50 caracteres.")
    private String primerNombre;

    @Size(max = 50, message = "El segundo nombre no puede tener más de 50 caracteres.")
    private String segundoNombre;

    @NotBlank(message = "El primer apellido no puede estar vacío.")
    @Size(max = 50, message = "El primer apellido no puede tener más de 50 caracteres.")
    private String primerApellido;

    @Size(max = 50, message = "El segundo apellido no puede tener más de 50 caracteres.")
    private String segundoApellido;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "La fecha de nacimiento no puede estar vacía.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "El email debe ser válido.")
    private String email;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 8, max = 100, message = "El nombre de usuario debe tener entre 8 y 100 caracteres.")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?-])[A-Za-z\\d!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?-]{8,100}$",
            message = "La contraseña debe tener entre 8 y 100 caracteres, incluyendo al menos una letra mayúscula, una letra minúscula, un número y un carácter especial."
    )
    private String contraseña;

    @NotBlank(message = "El confirmar contraseña no puede estar vacío.")
    private String confirmarContraseña;

}
