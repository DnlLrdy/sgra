package com.project.sgra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.model.Parada;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sgra/admin/rutas")
public class RutaController {

    @Autowired
    private RutaRepository rutaRepository;

    @GetMapping
    public String listarRutas(Model model) {
        model.addAttribute("rutas", rutaRepository.findAll());
        return "admin/rutas/listar-rutas";
    }

    @GetMapping("/agregar")
    public String agregarRuta(Model model) {
        model.addAttribute("ruta", new Ruta());
        return "admin/rutas/agregar-ruta";
    }

    @PostMapping("/guardar")
    public String guardarRuta(@RequestParam String nombre, @RequestParam String paradasJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Parada> paradas = mapper.readValue(paradasJson, new TypeReference<List<Parada>>() {
        });

        Ruta ruta = new Ruta();
        ruta.setNombre(nombre);
        ruta.setParadas(paradas);

        rutaRepository.save(ruta);
        return "redirect:/sgra/admin/rutas";
    }

}
