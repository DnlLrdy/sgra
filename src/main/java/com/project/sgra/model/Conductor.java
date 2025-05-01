package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "conductores")
public class Conductor extends Persona {

    @Id
    private String id;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String numeroLicencia;
    private LocalDate fechaVencimientoLicencia;
    private EstadoConductor estado;
    private boolean disponibilidad;
    private final String rol = "CONDUCTOR";

    @Override
    public String getRol() {
        return this.rol;
    }

    public enum TipoDocumento {
        CC,   // Cédula de ciudadanía
        CE,   // Cédula de extranjería
    }

    public enum EstadoConductor {
        ACTIVO, INACTIVO, SUSPENDIDO, LICENCIA_VENCIDA
    }

}
