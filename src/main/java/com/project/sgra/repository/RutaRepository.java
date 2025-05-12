package com.project.sgra.repository;

import com.project.sgra.model.Ruta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutaRepository extends MongoRepository<Ruta, String> {
    Optional<Ruta> findByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, String id);
}
