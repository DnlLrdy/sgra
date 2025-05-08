package com.project.sgra.repository;

import com.project.sgra.model.Autobus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutobusRepository extends MongoRepository<Autobus, String> {
    List<Autobus> findByRutaId(String rutaId);

    Optional<Autobus> findByMatricula(String matricula);
}
