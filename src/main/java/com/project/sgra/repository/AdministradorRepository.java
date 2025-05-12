package com.project.sgra.repository;

import com.project.sgra.model.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends MongoRepository<Administrador, String> {
    Optional<Administrador> findByNombreUsuario(String nombreUsuario);

    boolean existsByCorreoElectronico(String correoElectronico);

    boolean existsByNombreUsuario(String nombreUsuario);
}
