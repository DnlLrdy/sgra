package com.project.sgra.controller;

import com.project.sgra.model.Ruta;
import com.project.sgra.repository.RutaRepository;
import com.project.sgra.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/sgra/usuario")
public class UsuarioBusquedaController {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private RutaService rutaService;

    @GetMapping("/buscar")
    public String buscarRutas(@RequestParam(required = false) String ubicacion,
                              @RequestParam(required = false) String destino,
                              Model model) {

        List<Ruta> rutas = Collections.emptyList();

        if (ubicacion != null && destino != null && !ubicacion.isEmpty() && !destino.isEmpty()) {
            rutas = rutaRepository.findRutasByUbicacionAndDestino(ubicacion, destino);
        }

        model.addAttribute("autobuses", rutas);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("destino", destino);

        return "usuario/buscar-rutas";
    }

}
