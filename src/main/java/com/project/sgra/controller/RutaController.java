package com.project.sgra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sgra.dto.ParadaDTO;
import com.project.sgra.dto.RutaDTO;
import com.project.sgra.model.Parada;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.RutaRepository;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    @GetMapping("/crear")
    public String crearRuta(Model model) {
        model.addAttribute("rutaDTO", new RutaDTO());
        return "admin/rutas/crear-ruta";
    }

    @PostMapping("/guardar")
    public String guardarRuta(@ModelAttribute("rutaDTO") RutaDTO rutaDTO,
                              BindingResult bindingResult,
                              @RequestParam("paradasJson") String paradasJson,
                              Model model) throws JsonProcessingException {

        if (rutaRepository.existsByNombre(rutaDTO.getNombre())) {
            bindingResult.rejectValue("nombre", null, "Ya existe una ruta con ese nombre.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ParadaDTO> paradas = Arrays.asList(mapper.readValue(paradasJson, ParadaDTO[].class));
            rutaDTO.setParadas(paradas);
        } catch (Exception e) {
            bindingResult.rejectValue("paradas", null, "Debes agregar al menos una parada");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RutaDTO>> violations = validator.validate(rutaDTO);

        for (ConstraintViolation<RutaDTO> violation : violations) {
            bindingResult.rejectValue(
                    violation.getPropertyPath().toString(),
                    null,
                    violation.getMessage()
            );
        }

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> mensajesError = new ArrayList<>();

            for (ObjectError error : errors) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    String field = fieldError.getField();  // ejemplo: paradas[2].nombre
                    String mensaje = fieldError.getDefaultMessage();

                    if (field.matches("paradas\\[\\d+\\]\\..+")) {
                        int index = Integer.parseInt(field.replaceAll("paradas\\[(\\d+)\\]\\..+", "$1"));
                        mensaje = "Error en la parada #" + (index + 1) + ": " + mensaje;
                    }

                    mensajesError.add(mensaje);
                }
            }

            model.addAttribute("message", mensajesError);
            model.addAttribute("rutaDTO", rutaDTO);
            model.addAttribute("paradasJson", paradasJson);
            return "admin/rutas/crear-ruta";
        }


        Ruta ruta = convertirDtoAEntidad(rutaDTO);
        rutaRepository.save(ruta);

        return "redirect:/sgra/admin/rutas";
    }


    @PostMapping("/modificar")
    public String modificarRuta(@RequestParam("id") String id, Model model) throws JsonProcessingException {
        Ruta ruta = rutaRepository.findById(id).orElse(null);

        ObjectMapper mapper = new ObjectMapper();
        String paradasJson = mapper.writeValueAsString(ruta.getParadas());

        model.addAttribute("ruta", ruta);
        model.addAttribute("paradasJson", paradasJson);
        return "admin/rutas/modificar-ruta";
    }

    @PostMapping("/actualizar")
    public String actualizarRuta(@ModelAttribute("ruta") Ruta ruta, @RequestParam String paradasJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Parada> paradas = mapper.readValue(paradasJson, new TypeReference<List<Parada>>() {
        });

        Ruta RutaModificar = rutaRepository.findById(ruta.getId()).orElse(null);

        RutaModificar.setNombre(ruta.getNombre());
        RutaModificar.setParadas(paradas);

        rutaRepository.save(RutaModificar);
        return "redirect:/sgra/admin/rutas";
    }

    @PostMapping("/eliminar")
    public String eliminarRuta(@RequestParam("id") String id) {
        rutaRepository.deleteById(id);
        return "redirect:/sgra/admin/rutas";
    }

    private Ruta convertirDtoAEntidad(RutaDTO dto) {
        Ruta ruta = new Ruta();
        ruta.setNombre(dto.getNombre());

        List<Parada> paradas = dto.getParadas().stream().map(p -> {
            Parada parada = new Parada();
            parada.setNombre(p.getNombre());
            parada.setLatitud(p.getLatitud());
            parada.setLongitud(p.getLongitud());
            parada.setColor(p.getColor());
            return parada;
        }).toList();

        ruta.setParadas(paradas);
        return ruta;
    }

}
