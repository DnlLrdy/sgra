package com.project.sgra.controller;

import com.project.sgra.dto.RestablecerContraseñaDTO;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import com.project.sgra.service.EmailService;
import com.project.sgra.service.RestablecerContraseñaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/restablecer-contrasena")
public class RestablecerContraseñaController {

    private static final String RESTABLECER_CONTRASEÑA_VISTA = "home-login-registro-contraseña/restablecer-contraseña";

    private static final String REDIRECT_RESTABLECER_CONTRASEÑA_VISTA = "redirect:/restablecer-contrasena";
    private static final String REDIRECT_LOGIN_VISTA = "redirect:/login";

    private final TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;
    private final RestablecerContraseñaService restablecerContraseñaService;
    private final EmailService emailService;

    public RestablecerContraseñaController(TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository,
                                           RestablecerContraseñaService restablecerContraseñaService,
                                           EmailService emailService) {

        this.tokenRestablecerContraseñaRepository = tokenRestablecerContraseñaRepository;
        this.restablecerContraseñaService = restablecerContraseñaService;
        this.emailService = emailService;
    }

    @GetMapping
    public String restablecerContraseña(Model model) {
        model.addAttribute("restablecerContraseñaDTO",
                model.containsAttribute("restablecerContraseñaDTO")
                        ? model.getAttribute("restablecerContraseñaDTO")
                        : new RestablecerContraseñaDTO());
        return RESTABLECER_CONTRASEÑA_VISTA;
    }

    @PostMapping("/restableciendo-contrasena")
    public String restableciendoContraseña(@ModelAttribute("restablecerContraseñaDTO") RestablecerContraseñaDTO restablecerContraseñaDTO,
                                           RedirectAttributes redirectAttributes) {

        LocalDateTime horaFechaActualColombia = LocalDateTime.now(ZoneId.of("America/Bogota"));

        List<String> mensajesError = restablecerContraseñaService.
                validarRestablecerContraseñaDTO(restablecerContraseñaDTO, restablecerContraseñaDTO.getCorreoElectronicoDTO(), horaFechaActualColombia);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("restablecerContraseñaDTO", restablecerContraseñaDTO);
            return REDIRECT_RESTABLECER_CONTRASEÑA_VISTA;
        }

        restablecerContraseñaService.eliminarTokensInactivos(restablecerContraseñaDTO.getCorreoElectronicoDTO(), horaFechaActualColombia);

        String token = UUID.randomUUID().toString();

        TokenRestablecerContraseña tokenRestablecerContraseña = restablecerContraseñaService.
                crearTokenRestablecerContraseña(token, restablecerContraseñaDTO.getCorreoElectronicoDTO(), horaFechaActualColombia);

        tokenRestablecerContraseñaRepository.save(tokenRestablecerContraseña);

        String resetLink = "http://localhost:8080/establecer-nueva-contrasena?token=" + token;
        emailService.sendResetLink(restablecerContraseñaDTO.getCorreoElectronicoDTO(), resetLink);

        redirectAttributes.addFlashAttribute("mensajeExito", "Te hemos enviado un enlace a tu correo electrónico para restablecer tu contraseña.");
        return REDIRECT_LOGIN_VISTA;
    }

}
