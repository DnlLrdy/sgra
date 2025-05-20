package com.project.sgra.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sgra/admin/dashboard")
public class DashboardController {

    private static final String DASHBOARD_VISTA = "admin/dashboard/dashboard";

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("administradorNombreUsuario", administradorNombreUsuario());
        return DASHBOARD_VISTA;
    }

    public String administradorNombreUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
