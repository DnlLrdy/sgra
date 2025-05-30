package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "rutas")
public class Ruta {

    @Id
    private String id;
    private String nombre;
    private List<Parada> paradas;

}
