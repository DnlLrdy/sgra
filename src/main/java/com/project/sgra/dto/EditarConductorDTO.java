package com.project.sgra.dto;

import com.project.sgra.model.Conductor;
import com.project.sgra.validator.CorreoElectronicoUnico;
import com.project.sgra.validator.NombreUsuarioUnico;
import com.project.sgra.validator.NumeroDocumentoUnico;
import com.project.sgra.validator.NumeroLicenciaUnico;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NumeroDocumentoUnico(message = "{conductorDTO.numeroDocumentoUnico}")
@NumeroLicenciaUnico(message = "{conductorDTO.numeroLicenciaUnico}")
@NombreUsuarioUnico(message = "{conductorDTO.nombreUsuarioDTO.nombreUsuarioUnico}")
@CorreoElectronicoUnico(message = "{usuarioDTO.correoElectronicoDTO.correoElectronicoUnico}")
public class EditarConductorDTO {

    public interface NotBlankAndNotNullValidator {
    }

    @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "{conductorDTO.idDTO.pattern}")
    private String idDTO;

    @NotBlank(message = "{conductorDTO.primerNombreDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(max = 50, message = "{conductorDTO.primerNombreDTO.size}")
    private String primerNombreDTO;

    @Size(max = 50, message = "{conductorDTO.segundoNombreDTO.size}")
    private String segundoNombreDTO;

    @NotBlank(message = "{conductorDTO.primerApellidoDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(max = 50, message = "{conductorDTO.primerApellidoDTO.size}")
    private String primerApellidoDTO;

    @Size(max = 50, message = "{conductorDTO.segundoApellidoDTO.size}")
    private String segundoApellidoDTO;

    @NotNull(message = "{conductorDTO.fechaNacimientoDTO.notNull}", groups = NotBlankAndNotNullValidator.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimientoDTO;

    @NotNull(message = "{conductorDTO.tipoDocumentoDTO.notNull}", groups = NotBlankAndNotNullValidator.class)
    private Conductor.TipoDocumento tipoDocumentoDTO;

    @NotBlank(message = "{conductorDTO.telefonoDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    private String numeroDocumentoDTO;

    @NotBlank(message = "{conductorDTO.numeroLicenciaDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    private String numeroLicenciaDTO;

    @NotNull(message = "{conductorDTO.fechaVencimientoLicenciaDTO.notNull}", groups = NotBlankAndNotNullValidator.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaVencimientoLicenciaDTO;

    @NotBlank(message = "{conductorDTO.correoElectronicoDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Email(message = "{conductorDTO.correoElectronicoDTO.email}")
    private String correoElectronicoDTO;

    @NotBlank(message = "{conductorDTO.telefonoDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Size(max = 10, message = "{conductorDTO.telefonoDTO.size}")
    private String telefonoDTO;

    @NotBlank(message = "{conductorDTO.nombreUsuarioDTO.notBlank}", groups = NotBlankAndNotNullValidator.class)
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "{conductorDTO.nombreUsuarioDTO.pattern.uppercase}"
    )
    @Pattern(
            regexp = ".*[a-z].*",
            message = "{conductorDTO.nombreUsuarioDTO.pattern.lowercase}"
    )
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9]{7,19}$",
            message = "{conductorDTO.nombreUsuarioDTO.pattern.structure}"
    )

    private String nombreUsuarioDTO;

}
