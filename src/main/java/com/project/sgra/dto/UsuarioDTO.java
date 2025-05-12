package com.project.sgra.dto;

import com.project.sgra.validator.CorreoElectronicoUnico;
import com.project.sgra.validator.NombreUsuarioUnico;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioDTO {

    public interface NotBlankAndNotNullValidator {
    }

    @NotBlank(message = "{usuarioDTO.primerNombreDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(max = 50, message = "{usuarioDTO.primerNombreDTO.size}")
    private String primerNombreDTO;

    @Size(max = 50, message = "{usuarioDTO.segundoNombreDTO.size}")
    private String segundoNombreDTO;

    @NotBlank(message = "{usuarioDTO.primerApellidoDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(max = 50, message = "{usuarioDTO.primerApellidoDTO.size}")
    private String primerApellidoDTO;

    @Size(max = 50, message = "{usuarioDTO.segundoApellidoDTO.size}")
    private String segundoApellidoDTO;

    @NotNull(message = "{usuarioDTO.fechaNacimientoDTO.notNull}", groups = NotBlankAndNotNullValidator.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimientoDTO;

    @NotBlank(message = "{usuarioDTO.correoElectronicoDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Email(message = "{usuarioDTO.correoElectronicoDTO.email}")
    @CorreoElectronicoUnico(message = "{usuarioDTO.correoElectronicoDTO.correoElectronicoUnico}")
    private String correoElectronicoDTO;

    @NotBlank(message = "{usuarioDTO.nombreUsuarioDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "{usuarioDTO.nombreUsuarioDTO.pattern.uppercase}"
    )
    @Pattern(
            regexp = ".*[a-z].*",
            message = "{usuarioDTO.nombreUsuarioDTO.pattern.lowercase}"
    )
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9]{7,19}$",
            message = "{usuarioDTO.nombreUsuarioDTO.pattern.structure}"
    )
    @NombreUsuarioUnico(message = "{usuarioDTO.nombreUsuarioDTO.nombreUsuarioUnico}")
    private String nombreUsuarioDTO;

    @NotBlank(message = "{usuarioDTO.contraseñaDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(min = 8, max = 100, message = "{usuarioDTO.contraseñaDTO.size}")
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "{usuarioDTO.contraseñaDTO.pattern.uppercase}"
    )
    @Pattern(
            regexp = ".*[a-z].*",
            message = "{usuarioDTO.contraseñaDTO.pattern.lowercase}"
    )
    @Pattern(
            regexp = ".*\\d.*",
            message = "{usuarioDTO.contraseñaDTO.pattern.digit}"
    )
    @Pattern(
            regexp = ".*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?-].*",
            message = "{usuarioDTO.contraseñaDTO.pattern.special}"
    )
    @Pattern(
            regexp = "^\\S+$",
            message = "{usuarioDTO.contraseñaDTO.pattern.noSpaces}"
    )
    private String contraseñaDTO;

    @NotBlank(message = "{usuarioDTO.confirmarContraseña.notBlank}", groups = NotBlankAndNotNullValidator.class)
    private String confirmarContraseñaDTO;

    @AssertTrue(message = "{usuarioDTO.isContraseñasCoincidenDTO.assertTrue}")
    public boolean isContraseñasCoincidenDTO() {
        return contraseñaDTO != null && contraseñaDTO.equals(confirmarContraseñaDTO);
    }

}
