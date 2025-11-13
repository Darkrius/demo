package com.example.demo.api.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inmobiliarias")
public class ControllerPrueba {




    @PostMapping
    public String hola() {return "Holaaa"   ;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public String holaAdmin() {
        return "¡Hola Admin! La seguridad del Gestion-Service funciona.";
    }
}
