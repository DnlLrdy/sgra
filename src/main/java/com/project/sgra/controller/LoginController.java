package com.project.sgra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final String LOGIN_VISTA = "home-login-registro-contraseña/login";

    private static final String REDIRECT_LOGIN_VISTA = "redirect:/login";
    private static final String FORWARD_LOGIN_VISTA = "forward:/login";

    @GetMapping
    public String login(@RequestParam(value = "error", required = false) String error, RedirectAttributes redirectAttributes) {
        if (error != null) {
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible iniciar sesión. Revisa tu nombre de usuario y contraseña e inténtalo otra vez.");
            return REDIRECT_LOGIN_VISTA;
        }

        return LOGIN_VISTA;
    }

    @PostMapping("/logging")
    public String logging(@RequestParam("nombreUsuario") String nombreUsuario,
                          @RequestParam("contraseña") String contraseña,
                          RedirectAttributes redirectAttributes) {

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                contraseña == null || contraseña.trim().isEmpty()) {

            redirectAttributes.addFlashAttribute("mensajeError", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            return REDIRECT_LOGIN_VISTA;
        }

        return FORWARD_LOGIN_VISTA;
    }

}
