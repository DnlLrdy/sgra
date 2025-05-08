package com.project.sgra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.dto.RutaDTO;
import com.project.sgra.model.Autobus;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.RutaRepository;
import com.project.sgra.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sgra/admin/rutas")
public class RutaController {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private RutaService rutaService;

    @Autowired
    private AutobusRepository autobusRepository;

    @GetMapping
    public String listarRutas(Model model) {
        model.addAttribute("rutas", rutaRepository.findAll());
        model.addAttribute("autobuses", autobusRepository.findAll());
        return "admin/rutas/listar-rutas";
    }

    @GetMapping("/crear")
    public String crearRuta(Model model) {
        model.addAttribute("rutaDTO", new RutaDTO());
        return "admin/rutas/crear-ruta";
    }

    @PostMapping("/guardar")
    public String guardarRuta(@ModelAttribute("rutaDTO") RutaDTO rutaDTO,
                              @RequestParam("paradasJson") String paradasJson,
                              BindingResult bindingResult,
                              Model model) {

        List<String> mensajesError = rutaService.validarRuta(rutaDTO, paradasJson, bindingResult);

        if (!mensajesError.isEmpty()) {
            model.addAttribute("mensajesError", mensajesError);
            model.addAttribute("rutaDTO", rutaDTO);
            model.addAttribute("paradasJson", paradasJson);
            return "admin/rutas/crear-ruta";
        }

        Ruta ruta = rutaService.convertirDtoAEntidad(rutaDTO);
        rutaRepository.save(ruta);

        return "redirect:/sgra/admin/rutas";
    }

    @PostMapping("/modificar")
    public String modificarRuta(@RequestParam("id") String id, Model model) throws JsonProcessingException {
        Ruta ruta = rutaRepository.findById(id).orElse(null);

        ObjectMapper mapper = new ObjectMapper();
        String paradasJson = mapper.writeValueAsString(ruta.getParadas());

        RutaDTO rutaDTO = new RutaDTO();
        rutaDTO.setId(ruta.getId());
        rutaDTO.setNombre(ruta.getNombre());

        model.addAttribute("rutaDTO", rutaDTO);
        model.addAttribute("paradasJson", paradasJson);
        return "admin/rutas/modificar-ruta";
    }

    @PostMapping("/actualizar")
    public String actualizarRuta(@ModelAttribute("rutaDTO") RutaDTO rutaDTO,
                                 @RequestParam("paradasJson") String paradasJson,
                                 BindingResult bindingResult,
                                 Model model) {

        List<String> mensajesError = rutaService.validarRuta(rutaDTO, paradasJson, bindingResult);

        if (!mensajesError.isEmpty()) {
            model.addAttribute("mensajesError", mensajesError);
            model.addAttribute("rutaDTO", rutaDTO);
            model.addAttribute("paradasJson", paradasJson);
            return "admin/rutas/modificar-ruta";
        }

        Ruta ruta = rutaService.convertirDtoAEntidad(rutaDTO);

        Ruta rutaExistente = rutaRepository.findById(rutaDTO.getId()).orElse(null);
        rutaExistente.setNombre(ruta.getNombre());
        rutaExistente.setParadas(ruta.getParadas());

        rutaRepository.save(rutaExistente);
        return "redirect:/sgra/admin/rutas";
    }

    @PostMapping("/eliminar")
    public String eliminarRuta(@RequestParam("id") String id) {
        List<Autobus> autobuses = autobusRepository.findByRutaId(id);

        for (Autobus autobus : autobuses) {
            autobus.setRutaId("");
            autobus.setRutaNombre("");
        }

        autobusRepository.saveAll(autobuses);
        rutaRepository.deleteById(id);
        return "redirect:/sgra/admin/rutas";
    }


    @PostMapping("/vincular")
    public String vincularAutobusRuta(@RequestParam("autobusesId") List<String> autobusesId,
                                     @RequestParam("rutaId") String rutaId,
                                     @RequestParam("rutaNombre") String rutaNombre) {

        List<Autobus> autobuses = autobusRepository.findAllById(autobusesId);

        for (Autobus autobus : autobuses) {
            autobus.setRutaId(rutaId);
            autobus.setRutaNombre(rutaNombre);
        }

        autobusRepository.saveAll(autobuses);

        return "redirect:/sgra/admin/rutas";
    }

    @PostMapping("/desvincular")
    public String desvincularAutobusRuta(@RequestParam("autobusId") String autobusId) {
        Autobus autobusExistente = autobusRepository.findById(autobusId).orElse(null);
        autobusExistente.setRutaId("");
        autobusExistente.setRutaNombre("");
        autobusRepository.save(autobusExistente);
        return "redirect:/sgra/admin/rutas";
    }


}
