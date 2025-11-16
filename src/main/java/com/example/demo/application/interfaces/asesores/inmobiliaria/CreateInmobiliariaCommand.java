package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.application.interfaces.asesores.proyecto.CreateProyectoCommand;
import com.example.demo.domain.entities.Proyecto;

import java.util.List;

public record CreateInmobiliariaCommand(
        String ruc,
        String razonSocial,
        List<CreateProyectoCommand> proyectos
) {
}
