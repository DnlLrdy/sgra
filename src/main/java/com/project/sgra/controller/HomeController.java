package com.project.sgra.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sgra/home")
public class HomeController {

    @GetMapping
    public String home() {
        return "home/home";
    }

}
