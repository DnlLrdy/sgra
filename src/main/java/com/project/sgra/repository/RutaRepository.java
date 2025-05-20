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

    @Query("{ 'paradas.nombre': { $all: [ { $regex: ?0, $options: 'i' }, { $regex: ?1, $options: 'i' } ] } }")
    List<Ruta> findRutasByUbicacionAndDestinoParcial(String ubicacion, String destino);

    @Query("{ 'paradas.nombre': { $regex: ?0, $options: 'i' } }")
    List<Ruta> findRutasByParadaParcial(String parada);

    boolean existsByNombreAndIdNot(String nombre, String id);

    default List<Ruta> findRutasByUbicacionAndDestino(String ubicacion, String destino) {
        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía");
        }
        if (destino == null || destino.isEmpty()) {
            throw new IllegalArgumentException("El destino no puede estar vacío");
        }
        return findRutasByUbicacionAndDestinoParcial(ubicacion, destino);
    }
}
