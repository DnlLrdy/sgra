package com.project.sgra.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPredictionController {

    @GetMapping("/prediccion")
    public String prediccion(Model model) {
        model.addAttribute("administradorNombreUsuario", administradorNombreUsuario());
        return "admin/prediccion/predict"; // -> src/main/resources/templates/admin/prediccion/predict.html
    }

    public String administradorNombreUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}