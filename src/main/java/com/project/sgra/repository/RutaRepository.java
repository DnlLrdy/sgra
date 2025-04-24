package com.project.sgra.repository;

import com.project.sgra.model.Ruta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RutaRepository extends MongoRepository<Ruta, String> {
    boolean existsByNombre(String nombre);
}
