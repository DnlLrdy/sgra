package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario extends Persona {

    @Id
    private String id;

    @Override
    public String getRol() {
        return "USUARIO";
    }

}
