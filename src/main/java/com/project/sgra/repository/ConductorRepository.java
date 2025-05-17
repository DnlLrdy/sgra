package com.project.sgra.repository;

import com.project.sgra.model.Conductor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConductorRepository extends MongoRepository<Conductor, String> {
    Optional<Conductor> findByCorreoElectronico(String correoElectronico);

    Optional<Conductor> findByNombreUsuario(String nombreUsuario);

    boolean existsByCorreoElectronico(String correoElectronico);

    boolean existsByNumeroDocumentoAndIdNot(String numeroDocumento, String id);

    boolean existsByNumeroLicenciaAndIdNot(String numeroLicencia, String id);

    boolean existsByNombreUsuarioAndIdNot(String nombreUsuario, String id);

    boolean existsByCorreoElectronicoAndIdNot(String correoElectronico, String id);

    @Query("{ 'estado': ?0, '_id': { '$nin': ?1 } }")
    List<Conductor> findByEstadoAndIdNotIn(Conductor.Estado estado, List<String> excludeIds);

}
