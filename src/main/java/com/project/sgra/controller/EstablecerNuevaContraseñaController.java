package com.project.sgra.controller;

import com.project.sgra.dto.EstablecerNuevaContraseñaDTO;
import com.project.sgra.model.Conductor;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.ConductorRepository;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import com.project.sgra.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/sgra/establecer-nueva-contraseña")
public class EstablecerNuevaContraseñaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String establecerNuevaContraseña(@RequestParam("token") String token, RedirectAttributes redirectAttributes, Model model) {
        Optional<TokenRestablecerContraseña> tokenRestablecerContraseña = tokenRestablecerContraseñaRepository.findByToken(token);

        if (tokenRestablecerContraseña.isEmpty() || tokenRestablecerContraseña.get().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            tokenRestablecerContraseñaRepository.deleteByToken(token);
            redirectAttributes.addFlashAttribute("mensajeError", "El enlace para restablecer la contraseña no es válido o ha expirado. Por favor, solicita uno nuevo.");
            return "redirect:/sgra/login";
        }

        EstablecerNuevaContraseñaDTO establecerNuevaContraseñaDTO = new EstablecerNuevaContraseñaDTO();
        establecerNuevaContraseñaDTO.setToken(token);

        model.addAttribute("establecerNuevaContraseñaDTO", establecerNuevaContraseñaDTO);
        return "restablecer-contraseña/establecer-nueva-contraseña";
    }

    @PostMapping("/estableciendo-nueva-contraseña")
    public String estableciendoNuevaContraseña(@RequestParam("token") String token,
                                               @ModelAttribute("establecerNuevaContraseñaDTO") EstablecerNuevaContraseñaDTO establecerNuevaContraseñaDTO,
                                               Model model,
                                               RedirectAttributes redirectAttributes,
                                               BindingResult bindingResult) {
        Optional<TokenRestablecerContraseña> tokenRestablecerContraseña = tokenRestablecerContraseñaRepository.findByToken(token);

        if (tokenRestablecerContraseña.isEmpty() || tokenRestablecerContraseña.get().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            tokenRestablecerContraseñaRepository.deleteByToken(token);
            redirectAttributes.addFlashAttribute("mensajeError", "El enlace para restablecer la contraseña no es válido o ha expirado. Por favor, solicita uno nuevo.");
            return "redirect:/sgra/login";
        }

        Optional<Usuario> usuario = usuarioRepository.findByEmail(tokenRestablecerContraseña.get().getEmailUsuario());
        Optional<Conductor> conductor = conductorRepository.findByEmail(tokenRestablecerContraseña.get().getEmailUsuario());

        if (usuario.isEmpty() && conductor.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No encontramos una cuenta asociada a ese email.");
            return "redirect:/sgra/login";
        }

        if (usuario.isPresent() && passwordEncoder.matches(establecerNuevaContraseñaDTO.getNuevaContraseña(), usuario.get().getContraseña())) {
            bindingResult.rejectValue("nuevaContraseña", null, "La nueva contraseña no puede ser la misma que la anterior.");
        }

        if (conductor.isPresent() && passwordEncoder.matches(establecerNuevaContraseñaDTO.getNuevaContraseña(), conductor.get().getContraseña())) {
            bindingResult.rejectValue("nuevaContraseña", null, "La nueva contraseña no puede ser la misma que la anterior.");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EstablecerNuevaContraseñaDTO>> violations = validator.validate(establecerNuevaContraseñaDTO);

        for (ConstraintViolation<EstablecerNuevaContraseñaDTO> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), null, violation.getMessage());
        }

        if (!bindingResult.hasFieldErrors("nuevaContraseña") &&
                !bindingResult.hasFieldErrors("confirmarNuevaContraseña") &&
                !establecerNuevaContraseñaDTO.getNuevaContraseña().equals(establecerNuevaContraseñaDTO.getConfirmarNuevaContraseña())) {
            bindingResult.rejectValue("confirmarContraseña", null, "Las nuevas contraseñas no coinciden.");
        }

        if (bindingResult.hasErrors()) {
            List<String> mensajesError = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> mensajesError.add(error.getDefaultMessage()));
            model.addAttribute("mensajesError", mensajesError);
            return "restablecer-contraseña/establecer-nueva-contraseña";
        }

        if (usuario.isPresent()) {
            usuario.get().setContraseña(passwordEncoder.encode(establecerNuevaContraseñaDTO.getNuevaContraseña()));
            usuarioRepository.save(usuario.get());
        }

        if (conductor.isPresent()) {
            conductor.get().setContraseña(passwordEncoder.encode(establecerNuevaContraseñaDTO.getNuevaContraseña()));
            conductorRepository.save(conductor.get());
        }

        tokenRestablecerContraseñaRepository.deleteByToken(token);

        redirectAttributes.addFlashAttribute("mensajeExito", "Tu nueva contraseña se estableció correctamente.");
        return "redirect:/sgra/login";
    }

}
