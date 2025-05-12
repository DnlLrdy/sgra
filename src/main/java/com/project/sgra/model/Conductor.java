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
    private Estado estado;
    private boolean disponibilidad;

    @Override
    public String getRol() {
        return "CONDUCTOR";
    }

    public enum TipoDocumento {
        CC("Cédula de ciudadanía"),
        CE("Cédula de extranjería");

        private final String tipoDocumento;

        TipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
        }

        @Override
        public String toString() {
            return this.tipoDocumento;
        }
    }

    public enum Estado {
        ACTIVO("Activo"),
        INACTIVO("Inactivo"),
        LICENCIA_VENCIDA("Licencia vencida"),
        SUSPENDIDO("Suspendido"),
        RETIRADO("Retirado");

        private final String estado;

        Estado(String estado) {
            this.estado = estado;
        }

        @Override
        public String toString() {
            return this.estado;
        }
    }

}
