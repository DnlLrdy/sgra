package com.project.sgra.controller;

import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/sgra/registro")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RegistroService registroService;

    @GetMapping
    public String registro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro/registro";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        List<String> mensajesError = registroService.validarUsuario(usuarioDTO, bindingResult);

        if (!mensajesError.isEmpty()) {
            model.addAttribute("mensajesError", mensajesError);
            model.addAttribute("usuarioDTO", usuarioDTO);
            return "registro/registro";
        }

        Usuario usuario = registroService.convertirDtoAEntidad(usuarioDTO);
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensajeExito", "Cuenta creada exitosamente.");
        return "redirect:/sgra/login";
    }

}
