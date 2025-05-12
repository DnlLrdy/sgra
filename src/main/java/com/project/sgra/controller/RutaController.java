package com.project.sgra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.dto.RutaDTO;
import com.project.sgra.model.Autobus;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.RutaRepository;
import com.project.sgra.service.RutaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sgra/admin/rutas")
public class RutaController {

    private static final String LISTAR_RUTAS_VISTA = "admin/rutas/listar-rutas";
    private static final String CREAR_RUTA_VISTA = "admin/rutas/crear-ruta";
    private static final String EDITAR_RUTA_VISTA = "admin/rutas/editar-ruta";

    private static final String REDIRECT_LISTAR_RUTAS_VISTA = "redirect:/sgra/admin/rutas";
    private static final String REDIRECT_CREAR_RUTA_VISTA = "redirect:/sgra/admin/rutas/crear";
    private static final String REDIRECT_EDITAR_RUTA_VISTA = "redirect:/sgra/admin/rutas/editar?id=";

    private final RutaRepository rutaRepository;
    private final AutobusRepository autobusRepository;
    private final RutaService rutaService;
    private final ObjectMapper objectMapper;

    public RutaController(RutaRepository rutaRepository, AutobusRepository autobusRepository, RutaService rutaService, ObjectMapper objectMapper) {
        this.rutaRepository = rutaRepository;
        this.autobusRepository = autobusRepository;
        this.rutaService = rutaService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String listarRutas(Model model) {
        model.addAttribute("rutas", rutaRepository.findAll());
        model.addAttribute("autobuses", autobusRepository.findAll());
        return LISTAR_RUTAS_VISTA;
    }

    @GetMapping("/crear")
    public String crearRuta(Model model) {
        model.addAttribute("rutaDTO", model.containsAttribute("rutaDTO") ? model.getAttribute("rutaDTO") : new RutaDTO());
        return CREAR_RUTA_VISTA;
    }

    @PostMapping("/guardar")
    public String guardarRuta(@ModelAttribute("rutaDTO") RutaDTO rutaDTO,
                              @RequestParam("paradasJson") String paradasJson,
                              RedirectAttributes redirectAttributes) {

        List<String> mensajesError = rutaService.validarRutaDTO(rutaDTO, paradasJson);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("rutaDTO", rutaDTO);
            redirectAttributes.addFlashAttribute("paradasJson", paradasJson);
            return REDIRECT_CREAR_RUTA_VISTA;
        }

        try {
            Ruta ruta = rutaService.convertirRutaDtoAEntidad(rutaDTO);
            rutaRepository.save(ruta);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ruta guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al guardar la ruta.");
        }

        return REDIRECT_LISTAR_RUTAS_VISTA;
    }

    @GetMapping("/editar")
    public String editarRuta(@RequestParam("id") String id, Model model) {
        Ruta ruta = rutaRepository.findById(id).orElse(null);
        if (ruta == null) {
            model.addAttribute("mensajeError", "La ruta no fue encontrada.");
            return REDIRECT_LISTAR_RUTAS_VISTA;
        }

        try {
            String paradasJson = objectMapper.writeValueAsString(ruta.getParadas());

            RutaDTO rutaDTO = new RutaDTO();
            rutaDTO.setIdDTO(ruta.getId());
            rutaDTO.setNombreDTO(ruta.getNombre());

            model.addAttribute("rutaDTO", rutaDTO);
            model.addAttribute("paradasJson", paradasJson);
        } catch (JsonProcessingException e) {
            model.addAttribute("mensajeError", "Hubo un error al procesar las paradas.");
            return REDIRECT_LISTAR_RUTAS_VISTA;
        }

        return EDITAR_RUTA_VISTA;
    }

    @PostMapping("/actualizar")
    public String actualizarRuta(@ModelAttribute("rutaDTO") RutaDTO rutaDTO,
                                 @RequestParam("paradasJson") String paradasJson,
                                 RedirectAttributes redirectAttributes) {

        List<String> mensajesError = rutaService.validarRutaDTO(rutaDTO, paradasJson);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("rutaDTO", rutaDTO);
            redirectAttributes.addFlashAttribute("paradasJson", paradasJson);
            return REDIRECT_EDITAR_RUTA_VISTA + rutaDTO.getIdDTO();
        }

        Ruta ruta = rutaService.convertirRutaDtoAEntidad(rutaDTO);
        Ruta rutaDB = rutaRepository.findById(rutaDTO.getIdDTO()).orElse(null);

        if (rutaDB != null) {
            try {
                rutaDB.setNombre(ruta.getNombre());
                rutaDB.setParadas(ruta.getParadas());
                rutaRepository.save(rutaDB);
                redirectAttributes.addFlashAttribute("mensajeExito", "Ruta actualizada correctamente.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensajeError", "Hubo un error al actualizar la ruta.");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "La ruta no existe.");
        }

        return REDIRECT_LISTAR_RUTAS_VISTA;
    }

    @PostMapping("/eliminar")
    public String eliminarRuta(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        try {
            List<Autobus> autobusesDB = autobusRepository.findByRutaId(id);

            if (!autobusesDB.isEmpty()) {
                for (Autobus autobus : autobusesDB) {
                    autobus.setRutaId("");
                    autobus.setRutaNombre("");
                }

                autobusRepository.saveAll(autobusesDB);
            }

            rutaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ruta eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un error al eliminar la ruta.");
        }

        return REDIRECT_LISTAR_RUTAS_VISTA;
    }

    @PostMapping("/vincular")
    public String vincularAutobusRuta(@RequestParam(required = false, name = "autobusesId") List<String> autobusesId,
                                      @RequestParam("rutaId") String rutaId,
                                      @RequestParam("rutaNombre") String rutaNombre,
                                      RedirectAttributes redirectAttributes) {

        if (autobusesId == null || autobusesId.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se seleccionaron autobuses para vincular.");
            return REDIRECT_LISTAR_RUTAS_VISTA;
        }

        try {
            List<Autobus> autobusesDB = autobusRepository.findAllById(autobusesId);

            if (autobusesDB.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensajeError", "No se encontraron autobusesDB con los IDs proporcionados.");
                return "redirect:/sgra/admin/rutas";
            }

            for (Autobus autobus : autobusesDB) {
                autobus.setRutaId(rutaId);
                autobus.setRutaNombre(rutaNombre);
            }

            autobusRepository.saveAll(autobusesDB);
            redirectAttributes.addFlashAttribute("mensajeExito", "Autobuses vinculados correctamente a la ruta.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un error al vincular los autobuses.");
        }

        return REDIRECT_LISTAR_RUTAS_VISTA;
    }


    @PostMapping("/desvincular")
    public String desvincularAutobusRuta(@RequestParam("autobusId") String autobusId, RedirectAttributes redirectAttributes) {
        try {
            Autobus autobusDB = autobusRepository.findById(autobusId).orElse(null);

            if (autobusDB == null) {
                redirectAttributes.addFlashAttribute("mensajeError", "El autobús no existe.");
            } else {
                if (autobusDB.getRutaId() != null && !autobusDB.getRutaId().isEmpty()) {
                    autobusDB.setRutaId("");
                    autobusDB.setRutaNombre("");

                    autobusRepository.save(autobusDB);
                    redirectAttributes.addFlashAttribute("mensajeExito", "Autobús desvinculado de la ruta correctamente.");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Hubo un error al desvincular el autobús.");
        }

        return REDIRECT_LISTAR_RUTAS_VISTA;
    }

}
