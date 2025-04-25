package com.project.sgra.repository;

import com.project.sgra.model.Ruta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RutaRepository extends MongoRepository<Ruta, String> {
    Optional<Ruta> findByNombre(String nombre);
}
