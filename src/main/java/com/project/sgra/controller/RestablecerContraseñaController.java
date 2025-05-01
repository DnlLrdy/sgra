package com.project.sgra.controller;

import com.project.sgra.model.Conductor;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/sgra/restablecer-contraseña")
public class RestablecerContraseñaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String restablecerContraseña () {
        return "restablecer-contraseña/restablecer-contraseña";
    }

    @PostMapping("/restableciendo-contraseña")
    public String restableciendoContraseña(@RequestParam String email, Model model, RedirectAttributes redirectAttributes) {
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("mensajeError", "El email no puede estar vacío.");
            return "restablecer-contraseña/restablecer-contraseña";
        }

        // Verificar si el correo está en Usuario
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        // Verificar si el correo está en Conductor
        Optional<Conductor> conductor = conductorRepository.findByEmail(email);

        if (usuario.isEmpty() && conductor.isEmpty()) {
            model.addAttribute("mensajeError", "No encontramos una cuenta asociada a ese email.");
            return "restablecer-contraseña/restablecer-contraseña";
        }

        ZoneId zonaColombia = ZoneId.of("America/Bogota");
        LocalDateTime ahoraColombia = LocalDateTime.now(zonaColombia);

        Optional<TokenRestablecerContraseña> tokenOpt = tokenRestablecerContraseñaRepository.findByEmailUsuarioAndFechaExpiracionAfter(email, ahoraColombia);

        if (tokenOpt.isPresent()) {
            model.addAttribute("mensajeError", "Ya hemos enviado anteriormente un enlace a tu email para restablecer tu contraseña.");
            return "restablecer-contraseña/restablecer-contraseña";
        }

        String token = UUID.randomUUID().toString();

        TokenRestablecerContraseña tokenRestablecerContraseña = new TokenRestablecerContraseña();
        tokenRestablecerContraseña.setToken(token);
        tokenRestablecerContraseña.setEmailUsuario(email);
        tokenRestablecerContraseña.setFechaExpiracion(ahoraColombia.plusMinutes(15));
        tokenRestablecerContraseñaRepository.save(tokenRestablecerContraseña);

        String resetLink = "http://localhost:8080/sgra/establecer-nueva-contraseña?token=" + token;
        emailService.sendResetLink(email, resetLink);

        redirectAttributes.addFlashAttribute("mensajeExito", "Te hemos enviado un enlace a tu email para restablecer tu contraseña.");
        return "redirect:/sgra/login";
    }

}
