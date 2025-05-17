package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "autobuses")
public class Autobus {

    @Id
    private String id;
    private String matricula;
    private String modelo;
    private int capacidad;
    private Estado estado;
    private String rutaId;
    private String rutaNombre;

    @DBRef
    private Conductor conductor;

    public enum Estado {
        ACTIVO("Activo"),
        EN_MANTENIMIENTO("En mantenimiento"),
        FUERA_DE_SERVICIO("Fuera de servicio");

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
