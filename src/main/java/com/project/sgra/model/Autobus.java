package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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
    private String estado;
    private String rutaId;
    private String rutaNombre;

}
