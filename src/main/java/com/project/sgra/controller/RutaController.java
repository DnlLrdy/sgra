package com.project.sgra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.dto.RutaDTO;
import com.project.sgra.model.Ruta;
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

    @GetMapping
    public String listarRutas(Model model) {
        model.addAttribute("rutas", rutaRepository.findAll());
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
        rutaRepository.deleteById(id);
        return "redirect:/sgra/admin/rutas";
    }

}
