package com.project.sgra.repository;

import com.project.sgra.model.Ruta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutaRepository extends MongoRepository<Ruta, String> {
    Optional<Ruta> findByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, String id);

    @Query("{ 'paradas.nombre': { $all: [?0, ?1] } }")
    List<Ruta> findRutasByUbicacionAndDestino(String ubicacion, String destino);

}
