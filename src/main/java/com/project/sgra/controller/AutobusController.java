package com.project.sgra.controller;

import com.project.sgra.dto.AutobusDTO;
import com.project.sgra.model.Autobus;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.service.AutobusService;
import com.project.sgra.service.ConductorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sgra/admin/autobuses")
public class AutobusController {

    private static final String LISTAR_AUTOBUSES_VISTA = "admin/autobuses/listar-autobuses";
    private static final String REDIRECT_LISTAR_AUTOBUSES_VISTA = "redirect:/sgra/admin/autobuses";

    private final AutobusRepository autobusRepository;
    private final ConductorService conductorService;
    private final AutobusService autobusService;

    public AutobusController(AutobusRepository autobusRepository, ConductorService conductorService, AutobusService autobusService) {
        this.autobusRepository = autobusRepository;
        this.conductorService = conductorService;
        this.autobusService = autobusService;
    }

    @GetMapping
    public String listarAutobuses(Model model) {
        model.addAttribute("autobuses", autobusRepository.findAll());
        model.addAttribute("conductores", conductorService.obtenerConductoresActivosSinAutobus());
        model.addAttribute("autobusDTO", model.containsAttribute("autobusDTO") ? model.getAttribute("autobusDTO") : new AutobusDTO());

        return LISTAR_AUTOBUSES_VISTA;
    }

    @PostMapping("/guardar")
    public String guardarAutobus(@ModelAttribute("autobusDTO") AutobusDTO autobusDTO,
                                 RedirectAttributes redirectAttributes) {

        List<String> mensajesError = autobusService.validarAutobusDTO(autobusDTO);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("autobusDTO", autobusDTO);
            redirectAttributes.addFlashAttribute("abrirModalAgregar", true);
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        try {
            Autobus autobus = autobusService.convertirAutobusDTOAEntidad(autobusDTO);
            autobusRepository.save(autobus);
            redirectAttributes.addFlashAttribute("mensajeExito", "Autobús guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al guardar el autobús.");
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        return REDIRECT_LISTAR_AUTOBUSES_VISTA;
    }

    @PostMapping("/actualizar")
    public String actualizarAutobus(@ModelAttribute("autobusDTO") AutobusDTO autobusDTO,
                                    RedirectAttributes redirectAttributes) {

        List<String> mensajesError = autobusService.validarAutobusDTO(autobusDTO);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("autobusDTO", autobusDTO);
            redirectAttributes.addFlashAttribute("abrirModalEditar", true);
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        Autobus autobusDB = autobusRepository.findById(autobusDTO.getIdDTO()).orElse(null);

        if (autobusDB == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "El autobús no existe.");
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        try {
            Autobus autobus = autobusService.convertirAutobusDTOAEntidad(autobusDTO);
            autobusService.actualizarAutobus(autobusDB, autobus);
            autobusRepository.save(autobusDB);
            redirectAttributes.addFlashAttribute("mensajeExito", "Autobús actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al actualizar el autobús.");
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        return REDIRECT_LISTAR_AUTOBUSES_VISTA;
    }

    @PostMapping("/eliminar")
    public String eliminarAutobus(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        Optional<Autobus> autobusOpt = autobusRepository.findById(id);

        if (autobusOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "El autobús no fue encontrado.");
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        Autobus autobusDB = autobusOpt.get();

        if (autobusDB.getRutaId() != null && !autobusDB.getRutaId().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No puedes eliminar un autobús que esté vinculado a una ruta.");
            return REDIRECT_LISTAR_AUTOBUSES_VISTA;
        }

        try {
            autobusRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "El autobús fue eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al intentar eliminar el autobús.");
        }

        return REDIRECT_LISTAR_AUTOBUSES_VISTA;
    }

    @PostMapping("/vincular-conductor")
    public String vincularConductor(@RequestParam String autobusId, @RequestParam String conductorId, RedirectAttributes redirectAttributes) {
        try {
            autobusService.vincularConductor(autobusId, conductorId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Conductor vinculado correctamente al autobús.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al vincular conductor.");
        }
        return "redirect:/sgra/admin/autobuses";
    }

    @PostMapping("/desvincular-conductor/{id}")
    @ResponseBody
    public ResponseEntity<String> desvincularConductor(@PathVariable("id") String autobusId) {
        Optional<Autobus> optionalAutobus = autobusRepository.findById(autobusId);

        if (optionalAutobus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autobús no encontrado");
        }

        try {
            Autobus autobus = optionalAutobus.get();

            if (autobus.getConductor() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El autobús no tiene un conductor asignado");
            }

            autobus.setConductor(null); // Desvincula conductor
            autobusRepository.save(autobus);

            return ResponseEntity.ok("Conductor desvinculado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al desvincular el conductor");
        }
    }


}
