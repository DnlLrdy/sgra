package com.project.sgra.service;

import com.project.sgra.model.Persona;
import com.project.sgra.repository.AdministradorRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Persona> findByUsername(String nombreUsuario) {
        Optional<? extends Persona> persona;

        persona = administradorRepository.findByNombreUsuario(nombreUsuario);
        if (persona.isPresent()) return Optional.of(persona.get());

        persona = conductorRepository.findByNombreUsuario(nombreUsuario);
        if (persona.isPresent()) return Optional.of(persona.get());

        persona = usuarioRepository.findByNombreUsuario(nombreUsuario);
        return persona.map(p -> (Persona) p);
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        System.out.println("Intentando autenticar al usuario: " + nombreUsuario);

        Persona persona = findByUsername(nombreUsuario)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado en la base de datos.");
                    return new UsernameNotFoundException("Usuario no encontrado.");
                });

        System.out.println("Usuario encontrado: " + persona.getNombreUsuario());

        return new User(
                persona.getNombreUsuario(),
                persona.getContraseña(),
                List.of(new SimpleGrantedAuthority("ROLE_" + persona.getRol()))
        );
    }

}
