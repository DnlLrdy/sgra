package com.project.sgra.repository;

import com.project.sgra.model.Autobus;
import com.project.sgra.model.Conductor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutobusRepository extends MongoRepository<Autobus, String> {
    List<Autobus> findByRutaId(String rutaId);

    boolean existsByMatriculaAndIdNot(String matricula, String id);

    List<Autobus> findByEstadoAndConductorIsNotNull(Autobus.Estado estado);

    Optional<Autobus> findByConductor(Conductor conductor);

    boolean existsByConductor(Conductor conductor);
}
