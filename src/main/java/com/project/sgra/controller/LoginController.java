package com.project.sgra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sgra/login")
public class LoginController {

    @GetMapping
    public String login(@RequestParam(value = "error", required = false) String error, RedirectAttributes redirectAttributes) {
        if (error != null) {
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible iniciar sesión. Revisa tu nombre de usuario y contraseña e inténtalo otra vez.");
            return "redirect:/sgra/login";
        }

        return "login/login";
    }

    @PostMapping("/logging")
    public String logging(@RequestParam("nombreUsuario") String nombreUsuario,
                          @RequestParam("contraseña") String contraseña,
                          RedirectAttributes redirectAttributes) {

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                contraseña == null || contraseña.trim().isEmpty()) {

            redirectAttributes.addFlashAttribute("mensajeError", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            return "redirect:/sgra/login";
        }

        return "forward:/sgra/login";
    }

}
