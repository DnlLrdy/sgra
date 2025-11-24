package com.project.sgra.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    private static final String HOME_VISTA = "home-login-registro-contraseña/home";

    @GetMapping
    public String home() {
        return HOME_VISTA;
    }

}
