package com.project.sgra.repository;

import com.project.sgra.model.Conductor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConductorRepository extends MongoRepository<Conductor, String> {
    Optional<Conductor> findByEmail(String email);
    Optional<Conductor> findByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsByNombreUsuario(String nombreUsuario);
}
