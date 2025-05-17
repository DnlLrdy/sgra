package com.project.sgra.repository;

import com.project.sgra.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    boolean existsByCorreoElectronico(String correoElectronico);

    boolean existsByNombreUsuarioAndIdNot(String nombreUsuario, String id);

    boolean existsByCorreoElectronicoAndIdNot(String correoElectronico, String id);
}
