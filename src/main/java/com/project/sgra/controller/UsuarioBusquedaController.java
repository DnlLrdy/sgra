package com.project.sgra.controller;

import com.project.sgra.model.Autobus;
import com.project.sgra.model.Parada;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.RutaRepository;
import com.project.sgra.service.BusquedaRutaService;
import com.project.sgra.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/sgra/usuario")
public class UsuarioBusquedaController {

    @Autowired
    private BusquedaRutaService busquedaRutaService;
    @Autowired
    private RutaRepository rutaRepository;
    @Autowired
    private AutobusRepository autobusRepository;

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

    @GetMapping("/ruta/{id}")
    @ResponseBody
    public Ruta obtenerRutaPorId(@PathVariable String id) {
        return rutaRepository.findById(id).orElse(null);
    }

    @GetMapping("/ruta/{id}/paradas")
    @ResponseBody
    public List<Parada> obtenerParadasPorRuta(@PathVariable String id) {
        Ruta ruta = rutaRepository.findById(id).orElse(null);
        if (ruta != null) {
            return ruta.getParadas();
        }
        return Collections.emptyList();
    }

    @GetMapping("/ruta/{rutaId}/autobuses")
    @ResponseBody
    public List<Autobus>obtenerAutobusesPorRuta(@PathVariable String rutaId) {
        return autobusRepository.findByRutaId(rutaId);
    }

}


