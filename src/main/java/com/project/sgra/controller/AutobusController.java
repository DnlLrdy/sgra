package com.project.sgra.controller;

import com.project.sgra.dto.AutobusDTO;
import com.project.sgra.model.Autobus;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.service.AutobusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sgra/admin/autobuses")
public class AutobusController {

    @Autowired
    private AutobusRepository autobusRepository;

    @Autowired
    private AutobusService autobusService;

    @GetMapping
    public String listarAutobuses(Model model) {
        model.addAttribute("autobuses", autobusRepository.findAll());
        if (!model.containsAttribute("autobusDTO")) {
            model.addAttribute("autobusDTO", new AutobusDTO());
        }
        return "admin/autobuses/listar-autobuses";
    }

    @PostMapping("/guardar")
    public String guardarAutobus(@ModelAttribute("autobusDTO") AutobusDTO autobusDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        List<String> mensajesError = autobusService.validarAutobus(autobusDTO, bindingResult);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("autobusDTO", autobusDTO);
            redirectAttributes.addFlashAttribute("abrirModalCrear", true);
            return "redirect:/sgra/admin/autobuses";
        }

        Autobus autobus = autobusService.convertirDtoAEntidad(autobusDTO);
        System.out.println(autobus.getEstado());
        autobusRepository.save(autobus);

        return "redirect:/sgra/admin/autobuses";
    }

    @PostMapping("/actualizar")
    public String actualizarAutobus(@ModelAttribute("autobusDTO") AutobusDTO autobusDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        List<String> mensajesError = autobusService.validarAutobus(autobusDTO, bindingResult);

        if (!mensajesError.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajesError", mensajesError);
            redirectAttributes.addFlashAttribute("autobusDTO", autobusDTO);
            redirectAttributes.addFlashAttribute("abrirModalEditar", true);
            return "redirect:/sgra/admin/autobuses";
        }

        Autobus autobus = autobusService.convertirDtoAEntidad(autobusDTO);

        Autobus autobuseExistente = autobusRepository.findById(autobusDTO.getId()).orElse(null);
        autobuseExistente.setMatricula(autobus.getMatricula());
        autobuseExistente.setModelo(autobus.getModelo());
        autobuseExistente.setCapacidad(autobus.getCapacidad());
        autobuseExistente.setEstado(autobus.getEstado());

        autobusRepository.save(autobuseExistente);

        return "redirect:/sgra/admin/autobuses";
    }

    @PostMapping("/eliminar")
    public String eliminarAutobus(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        Optional<Autobus> optionalAutobus = autobusRepository.findById(id);

        if (optionalAutobus.isPresent()) {
            Autobus autobus = optionalAutobus.get();

            if (autobus.getRutaId() != null && !autobus.getRutaId().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensajesError",
                        List.of("No puedes eliminar un autobús que esté vinculado a una ruta."));
                return "redirect:/sgra/admin/autobuses";
            }

            autobusRepository.deleteById(id);
        }

        return "redirect:/sgra/admin/autobuses";
    }


}
