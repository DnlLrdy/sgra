package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class Persona {

    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String nombreUsuario;
    private String contraseña;

    public abstract String getRol();

}
