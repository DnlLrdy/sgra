package com.project.sgra.controller;

import com.project.sgra.model.Ruta;
import com.project.sgra.service.BusquedaRutaService;
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
    private BusquedaRutaService busquedaRutaService;

    @GetMapping("/buscar")
    public String buscarRutas(@RequestParam(required = false) String ubicacion,
                              @RequestParam(required = false) String destino,
                              Model model) {

        List<Ruta> rutas = Collections.emptyList();
        String errorMessage = null;

        // Solo buscar si ambos campos están llenos
        if (ubicacion != null && destino != null &&
                !ubicacion.isEmpty() && !destino.isEmpty()) {
            rutas = busquedaRutaService.buscarRutasPorUbicacionYDestino(ubicacion, destino);
        } else if ((ubicacion != null && !ubicacion.isEmpty()) || (destino != null && !destino.isEmpty())) {
            errorMessage = "Debe ingresar ubicación y destino para buscar rutas.";
        }

        model.addAttribute("autobuses", rutas);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("destino", destino);
        model.addAttribute("errorMessage", errorMessage);

        return "usuario/buscar-rutas";
    }


    }


