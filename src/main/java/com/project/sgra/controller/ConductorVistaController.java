package com.project.sgra.controller;

import com.project.sgra.model.Autobus;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.RutaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/sgra/conductor")
public class ConductorVistaController {

    private static final String CONDUCTOR_VISTA = "conductor/conductor";
    private static final String REDIRECT_CONDUCTOR_VISTA = "redirect:/sgra/conductor";

    private final AutobusRepository autobusRepository;
    private final RutaRepository rutaRepository;

    public ConductorVistaController(AutobusRepository autobusRepository, RutaRepository rutaRepository) {
        this.autobusRepository = autobusRepository;
        this.rutaRepository = rutaRepository;
    }

    @GetMapping
    public String conductor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String conductorNombreUsuario = authentication.getName();

        Optional<Autobus> autobusOpt = autobusRepository.findByConductorNombreUsuario(conductorNombreUsuario);

        if (autobusOpt.isPresent()) {
            Autobus autobus = autobusOpt.get();

            Optional<Ruta> rutaOpt = rutaRepository.findById(autobus.getRutaId());

            if (rutaOpt.isPresent()) {
                Ruta ruta = rutaOpt.get();
                model.addAttribute("autobus", autobus);
                model.addAttribute("ruta", ruta);
            } else {
                model.addAttribute("error", "Ruta no encontrada para el autobús.");
            }
        } else {
            model.addAttribute("error", "No se encontró un autobús para este conductor.");
        }

        model.addAttribute("conductorNombreUsuario", conductorNombreUsuario);
        return CONDUCTOR_VISTA;
    }

}
