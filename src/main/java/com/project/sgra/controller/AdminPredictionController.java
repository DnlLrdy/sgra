package com.project.sgra.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sgra/admin")
public class AdminPredictionController {

    @GetMapping("/prediccion")
    public String prediccion(Model model) {
        // puedes añadir atributos al modelo si necesitas (por ejemplo, opciones para selects)
        return "admin/prediccion/predict"; // -> src/main/resources/templates/admin/prediccion/predict.html
    }
}