package com.project.sgra.controller;

import com.project.sgra.dto.UsuarioDTO;
import com.project.sgra.model.TokenRestablecerContraseña;
import com.project.sgra.model.Usuario;
import com.project.sgra.repository.TokenRestablecerContraseñaRepository;
import com.project.sgra.repository.UsuarioRepository;
import com.project.sgra.service.EmailService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/sgra")
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRestablecerContraseñaRepository tokenRestablecerContraseñaRepository;

    @Autowired
    private EmailService emailService;









}
