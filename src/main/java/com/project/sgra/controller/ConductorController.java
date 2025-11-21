package com.project.sgra.controller;

import com.project.sgra.dto.ConductorDTO;
import com.project.sgra.dto.EditarConductorDTO;
import com.project.sgra.model.Autobus;
import com.project.sgra.model.Conductor;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.service.ConductorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/conductores")
public class ConductorController {

    private static final String LISTAR_CONDUCTORES_VISTA = "admin/conductores/listar-conductores";
    private static final String REDIRECT_LISTAR_CONDUCTORES_VISTA = "redirect:/admin/conductores";

    private final ConductorRepository conductorRepository;
    private final AutobusRepository autobusRepository;
    private final ConductorService conductorService;

    public ConductorController(ConductorRepository conductorRepository, AutobusRepository autobusRepository, ConductorService conductorService) {
        this.conductorRepository = conductorRepository;
        this.autobusRepository = autobusRepository;
        this.conductorService = conductorService;
    }

    @GetMapping
    public String listarConductores(Model model) {
        List<Conductor> conductores = conductorRepository.findAll();
        List<Autobus> autobuses = autobusRepository.findAll();

        Map<String, String> matriculasPorConductor = new HashMap<>();
        for (Autobus a : autobuses) {
            if (a.getConductor() != null) {
                matriculasPorConductor.put(a.getConductor().getId(), a.getMatricula());
            }
        }

        model.addAttribute("conductores", conductores);
        model.addAttribute("matriculasPorConductor", matriculasPorConductor);

        model.addAttribute("conductorDTO", model.containsAttribute("conductorDTO") ? model.getAttribute("conductorDTO") : new ConductorDTO());
        model.addAttribute("editarConductorDTO", model.containsAttribute("editarConductorDTO") ? model.getAttribute("editarConductorDTO") : new EditarConductorDTO());

        model.addAttribute("administradorNombreUsuario", administradorNombreUsuario());
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
    public String actualizarConductor(@ModelAttribute("editarConductorDTO") EditarConductorDTO editarConductorDTO,
                                      RedirectAttributes redirectAttributes) {

        List<String> mensajesError = conductorService.validarEditarCoductorDTO(editarConductorDTO);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("editarConductorDTO", editarConductorDTO);
            redirectAttributes.addFlashAttribute("abrirModalEditar", true);
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        Conductor conductorDB = conductorRepository.findById(editarConductorDTO.getIdDTO()).orElse(null);
        Autobus autobus = autobusRepository.findByConductor(conductorDB).orElse(null);

        if (conductorDB == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "El conductor no existe.");
            return REDIRECT_LISTAR_CONDUCTORES_VISTA;
        }

        try {
            Conductor conductor = conductorService.convertirEditarConductorDTOAEntidad(editarConductorDTO);
            conductorService.actualizarConductor(conductorDB, conductor);
            autobus.setConductor(conductorDB);

            conductorRepository.save(conductorDB);
            autobusRepository.save(autobus);
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

        Conductor conductor = conductorOpt.get();

        boolean conductorAsignado = autobusRepository.existsByConductor(conductor);

        if (conductorAsignado) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar el conductor porque está asignado a un autobús.");
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


    @PostMapping("/actualizar-estado")
    @ResponseBody
    public ResponseEntity<?> actualizarEstadoConductor(@RequestParam("id") String id, @RequestParam("estado") String estadoStr) {
        Optional<Conductor> optionalConductor = conductorRepository.findById(id);

        if (optionalConductor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conductor no encontrado");
        }

        Conductor conductor = optionalConductor.get();
        Autobus autobus = autobusRepository.findByConductor(conductor).orElse(null);

        try {
            Conductor.Estado nuevoEstado = Conductor.Estado.valueOf(estadoStr);

            if (nuevoEstado == Conductor.Estado.ACTIVO) {
                conductor.setEstado(nuevoEstado);
                conductor.setDisponibilidad(true);

                autobus.getConductor().setEstado(nuevoEstado);
                autobus.getConductor().setDisponibilidad(true);

                conductorRepository.save(conductor);
                autobusRepository.save(autobus);
            } else {
                conductor.setEstado(nuevoEstado);
                conductor.setDisponibilidad(false);

                autobus.getConductor().setEstado(nuevoEstado);
                autobus.getConductor().setDisponibilidad(false);

                conductorRepository.save(conductor);
                autobusRepository.save(autobus);
            }

            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado");
        }

    }

    public String administradorNombreUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
