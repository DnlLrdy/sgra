package com.project.sgra.repository;

import com.project.sgra.model.TokenRestablecerContraseña;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRestablecerContraseñaRepository extends MongoRepository<TokenRestablecerContraseña, String> {
    List<TokenRestablecerContraseña> findByCorreoElectronicoAndFechaExpiracionBefore(String correo, LocalDateTime fechaExpiracion);

    Optional<TokenRestablecerContraseña> findByToken(String token);

    boolean existsByCorreoElectronicoAndFechaExpiracionAfter(String correo, LocalDateTime fechaExpiracion);

    void deleteByToken(String token);
}
