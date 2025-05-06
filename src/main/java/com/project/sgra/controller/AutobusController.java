package com.project.sgra.controller;

import com.project.sgra.model.Autobus;
import com.project.sgra.repository.AutobusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/sgra/admin/autobuses")
public class AutobusController {

    @Autowired
    private AutobusRepository autobusRepository;

    @GetMapping
    public String listarAutobuses(Model model) {
        List<Autobus> autobuses = autobusRepository.findAll();
        model.addAttribute("autobuses", autobuses);
        model.addAttribute("autobus", new Autobus());
        return "admin/autobuses/listar-autobuses";
    }

    @PostMapping("/guardar")
    public String guardarAutobus(@ModelAttribute("autobus") Autobus autobus) {
        autobusRepository.save(autobus);
        return "redirect:/sgra/admin/autobuses";
    }

    @PostMapping("/actualizar")
    public String actualizarAutobus(@RequestParam("id") String id,
                                    @RequestParam("matricula") String matricula,
                                    @RequestParam("modelo") String modelo,
                                    @RequestParam("capacidad") int capacidad,
                                    @RequestParam("estado") String estado) {

        Autobus autobusExistente = autobusRepository.findById(id).orElse(null);
        autobusExistente.setMatricula(matricula);
        autobusExistente.setModelo(modelo);
        autobusExistente.setCapacidad(capacidad);
        autobusExistente.setEstado(estado);

        autobusRepository.save(autobusExistente);
        return "redirect:/sgra/admin/autobuses";
    }

    @PostMapping("/eliminar")
    public String eliminarAutobus(@RequestParam("id") String id) {
        autobusRepository.deleteById(id);
        return "redirect:/sgra/admin/autobuses";
    }

}
