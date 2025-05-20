package com.project.sgra.controller;

import com.project.sgra.dto.EstablecerNuevaContraseñaDTO;
import com.project.sgra.model.Conductor;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.service.EstablecerNuevaContraseñaService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sgra/establecer-nueva-contrasena")
public class EstablecerNuevaContraseñaController {

    private static final String ESTABLECER_NUEVA_CONTRASEÑA_VISTA = "home-login-registro-contraseña/establecer-nueva-contraseña";

    private static final String REDIRECT_ESTABLECER_NUEVA_CONTRASEÑA_VISTA = "redirect:/sgra/establecer-nueva-contrasena?token=";
    private static final String REDIRECT_LOGIN_VISTA = "redirect:/sgra/login";

    private static final String MENSAJE_TOKEN_INVALIDO = "El enlace para restablecer la contraseña no es válido o ha expirado. Por favor, solicita uno nuevo.";
    private static final String MENSAJE_EXITO = "Tu nueva contraseña se estableció correctamente.";

    private final TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;
    private final EstablecerNuevaContraseñaService establecerNuevaContraseñaService;
    private final PasswordEncoder passwordEncoder;

    public EstablecerNuevaContraseñaController(TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository,
                                               UsuarioRepository usuarioRepository,
                                               ConductorRepository conductorRepository,
                                               EstablecerNuevaContraseñaService establecerNuevaContraseñaService,
                                               PasswordEncoder passwordEncoder) {

        this.tokenRestablecerContraseñaRepository = tokenRestablecerContraseñaRepository;
        this.usuarioRepository = usuarioRepository;
        this.conductorRepository = conductorRepository;
        this.establecerNuevaContraseñaService = establecerNuevaContraseñaService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String mostrarFormularioEstablecerContraseña(@RequestParam("token") String token,
                                                        RedirectAttributes redirectAttributes,
                                                        Model model) {
        if (esTokenValido(token)) {
            redirectAttributes.addFlashAttribute("mensajeError", MENSAJE_TOKEN_INVALIDO);
            return REDIRECT_LOGIN_VISTA;
        }

        model.addAttribute("establecerNuevaContraseñaDTO",
                model.containsAttribute("establecerNuevaContraseñaDTO")
                        ? model.getAttribute("establecerNuevaContraseñaDTO")
                        : crearDTOConToken(token));

        return ESTABLECER_NUEVA_CONTRASEÑA_VISTA;
    }


    @PostMapping("/estableciendo-nueva-contrasena")
    public String establecerNuevaContraseña(@ModelAttribute("establecerNuevaContraseñaDTO") EstablecerNuevaContraseñaDTO dto,
                                            RedirectAttributes redirectAttributes) {

        if (esTokenValido(dto.getTokenDTO())) {
            redirectAttributes.addFlashAttribute("mensajeError", MENSAJE_TOKEN_INVALIDO);
            return REDIRECT_LOGIN_VISTA;
        }

        List<String> mensajesError = establecerNuevaContraseñaService.validarEstablecerNuevaContraseñaDTO(dto);
        Optional<TokenRestablecerContraseña> tokenOpt = tokenRestablecerContraseñaRepository.findByToken(dto.getTokenDTO());

        if (tokenOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", MENSAJE_TOKEN_INVALIDO);
            return REDIRECT_LOGIN_VISTA;
        }

        String correo = tokenOpt.get().getCorreoElectronico();
        String nuevaContraseñaEnTextoPlano = dto.getNuevaContraseñaDTO();

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("establecerNuevaContraseñaDTO", dto);
            return REDIRECT_ESTABLECER_NUEVA_CONTRASEÑA_VISTA + dto.getTokenDTO();
        }

        if (esMismaContraseñaRegistrada(correo, nuevaContraseñaEnTextoPlano)) {
            redirectAttributes.addFlashAttribute("mensajesError", List.of("La nueva contraseña no puede ser igual a la anterior."));
            redirectAttributes.addFlashAttribute("establecerNuevaContraseñaDTO", dto);
            return REDIRECT_ESTABLECER_NUEVA_CONTRASEÑA_VISTA + dto.getTokenDTO();
        }

        actualizarContraseñaSiExiste(correo, nuevaContraseñaEnTextoPlano);
        tokenRestablecerContraseñaRepository.deleteByToken(dto.getTokenDTO());

        redirectAttributes.addFlashAttribute("mensajeExito", MENSAJE_EXITO);
        return REDIRECT_LOGIN_VISTA;
    }


    private boolean esTokenValido(String token) {
        return !establecerNuevaContraseñaService.validarToken(token);
    }

    private EstablecerNuevaContraseñaDTO crearDTOConToken(String token) {
        EstablecerNuevaContraseñaDTO dto = new EstablecerNuevaContraseñaDTO();
        dto.setTokenDTO(token);
        return dto;
    }

    private void actualizarContraseñaSiExiste(String correo, String nuevaContraseña) {
        usuarioRepository.findByCorreoElectronico(correo)
                .ifPresent(usuario -> {
                    usuario.setContraseña(passwordEncoder.encode(nuevaContraseña));
                    usuarioRepository.save(usuario);
                });

        conductorRepository.findByCorreoElectronico(correo)
                .ifPresent(conductor -> {
                    conductor.setContraseña(passwordEncoder.encode(nuevaContraseña));
                    conductorRepository.save(conductor);
                });
    }

    private boolean esMismaContraseñaRegistrada(String correo, String nuevaContraseña) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreoElectronico(correo);
        Optional<Conductor> conductor = conductorRepository.findByCorreoElectronico(correo);

        return usuario.map(u -> passwordEncoder.matches(nuevaContraseña, u.getContraseña())).orElse(false) ||
                conductor.map(c -> passwordEncoder.matches(nuevaContraseña, c.getContraseña())).orElse(false);
    }

}
