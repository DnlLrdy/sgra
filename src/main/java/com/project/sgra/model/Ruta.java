package com.project.sgra.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "rutas")
public class Ruta {

    @Id
    private String id;
    private String nombre;
    private List<Parada> paradas;

}
