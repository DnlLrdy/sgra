package com.project.sgra.repository;

import com.project.sgra.model.TokenRestablecerContraseña;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRestablecerContraseñaRepository extends MongoRepository<TokenRestablecerContraseña, String> {
    Optional<TokenRestablecerContraseña> findByToken(String token);

    Optional<TokenRestablecerContraseña> findByEmailUsuarioAndFechaExpiracionAfter(String email, LocalDateTime fechaExpiracion);

    void deleteByToken(String token);
}
