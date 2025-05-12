package com.project.sgra.service;

import com.project.sgra.model.Persona;
import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;
    private final AdministradorRepository administradorRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    ConductorRepository conductorRepository,
                                    AdministradorRepository administradorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.conductorRepository = conductorRepository;
        this.administradorRepository = administradorRepository;
    }

    public Optional<Persona> findByUsername(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .map(p -> (Persona) p)
                .or(() -> conductorRepository.findByNombreUsuario(nombreUsuario).map(p -> (Persona) p))
                .or(() -> administradorRepository.findByNombreUsuario(nombreUsuario).map(p -> (Persona) p));
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        logger.info("Intentando autenticar al usuario: {}", nombreUsuario);

        Persona persona = findByUsername(nombreUsuario)
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado en la base de datos: {}", nombreUsuario);
                    return new UsernameNotFoundException("Usuario no encontrado.");
                });

        logger.info("Usuario autenticado correctamente: {}", persona.getNombreUsuario());

        return new User(
                persona.getNombreUsuario(),
                persona.getContraseña(),
                List.of(new SimpleGrantedAuthority("ROLE_" + persona.getRol()))
        );
    }

}
