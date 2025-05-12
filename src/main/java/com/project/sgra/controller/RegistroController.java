package com.project.sgra.controller;

import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sgra/registro")
public class RegistroController {

    private static final String REGISTRO_VISTA = "home-login-registro-contraseña/registro";

    private static final String REDIRECT_REGISTRO_VISTA = "redirect:/sgra/registro";
    private static final String REDIRECT_LOGIN_VISTA = "redirect:/sgra/login";

    private final UsuarioRepository usuarioRepository;
    private final RegistroService registroService;

    public RegistroController(UsuarioRepository usuarioRepository, RegistroService registroService) {
        this.usuarioRepository = usuarioRepository;
        this.registroService = registroService;
    }

    @GetMapping
    public String registro(Model model) {
        model.addAttribute("usuarioDTO", model.containsAttribute("usuarioDTO") ? model.getAttribute("usuarioDTO") : new UsuarioDTO());
        return REGISTRO_VISTA;
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
                            RedirectAttributes redirectAttributes) {

        List<String> mensajesError = registroService.validarUsuarioDTO(usuarioDTO);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("usuarioDTO", usuarioDTO);
            return REDIRECT_REGISTRO_VISTA;
        }

        Usuario usuario = registroService.convertirUsuarioDTOAEntidad(usuarioDTO);
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensajeExito", "Cuenta creada exitosamente.");
        return REDIRECT_LOGIN_VISTA;
    }

}
