package com.project.sgra.repository;

import com.project.sgra.model.Autobus;
import com.project.sgra.model.Conductor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutobusRepository extends MongoRepository<Autobus, String> {
    List<Autobus> findByRutaId(String rutaId);

    boolean existsByMatriculaAndIdNot(String matricula, String id);

    List<Autobus> findByConductor(Conductor conductor);
}
