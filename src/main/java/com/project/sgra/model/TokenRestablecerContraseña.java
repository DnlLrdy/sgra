package com.project.sgra.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "token_restablecer_contraseña")
public class TokenRestablecerContraseña {

    @Id
    private String id;

    private String token;

    private String emailUsuario;

    private LocalDateTime fechaExpiracion;

}
