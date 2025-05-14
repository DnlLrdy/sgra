package com.project.sgra.controller;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.model.Conductor;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.service.ConductorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sgra/admin/conductores")
public class ConductorController {

    private static final String LISTAR_CONDUCTORES_VISTA = "admin/conductores/listar-conductores";
    private static final String REDIRECT_LISTAR_CONDUCTORES_VISTA = "redirect:/sgra/admin/conductores";

    private final ConductorRepository conductorRepository;
    private final ConductorService conductorService;

    public ConductorController(ConductorRepository conductorRepository, ConductorService conductorService) {
        this.conductorRepository = conductorRepository;
        this.conductorService = conductorService;
    }

    @GetMapping
    public String listarConductores(Model model) {
        model.addAttribute("conductores", conductorRepository.findAll());
        model.addAttribute("conductorDTO", model.containsAttribute("conductorDTO") ? model.getAttribute("conductorDTO") : new ConductorDTO());

        return LISTAR_CONDUCTORES_VISTA;
    }

    @PostMapping("/guardar")
    public String guardarConductor(@ModelAttribute("conductorDTO") ConductorDTO conductorDTO,
                                   RedirectAttributes redirectAttributes) {

        List<String> mensajesError = conductorService.validarCoductorDTO(conductorDTO);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("conductorDTO", conductorDTO);
            redirectAttributes.addFlashAttribute("abrirModalAgregar", true);
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        try {
            Conductor conductor = conductorService.convertirConductorDTOAEntidad(conductorDTO);
            conductor.setEstado(Conductor.Estado.INACTIVO);
            conductor.setDisponibilidad(false);
            conductorRepository.save(conductor);
            redirectAttributes.addFlashAttribute("mensajeExito", "Conductor guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al guardar el conductor.");
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        return REDIRECT_LISTAR_CONDUCTORES_VISTA;
    }

    @PostMapping("/actualizar")
    public String actualizarConductor(@ModelAttribute("conductorDTO") ConductorDTO conductorDTO,
                                      RedirectAttributes redirectAttributes) {

        List<String> mensajesError = conductorService.validarCoductorDTO(conductorDTO);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("conductorDTO", conductorDTO);
            redirectAttributes.addFlashAttribute("abrirModalEditar", true);
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        Conductor conductorDB = conductorRepository.findById(conductorDTO.getIdDTO()).orElse(null);

        if (conductorDB == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "El conductor no existe.");
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        try {
            Conductor conductor = conductorService.convertirConductorDTOAEntidad(conductorDTO);
            conductorService.actualizarConductor(conductorDB, conductor);
            conductorRepository.save(conductorDB);
            redirectAttributes.addFlashAttribute("mensajeExito", "Conductor actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al actualizar el coductor.");
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        return REDIRECT_LISTAR_CONDUCTORES_VISTA;
    }

    @PostMapping("/eliminar")
    public String eliminarConductor(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        Optional<Conductor> conductorOpt = conductorRepository.findById(id);

        if (conductorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "El conductor no fue encontrado.");
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        try {
            conductorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "El conductor fue eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al intentar eliminar el conductor.");
        }

        return REDIRECT_LISTAR_CONDUCTORES_VISTA;
    }

}
