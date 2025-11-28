package com.example.demo.application.dto.commands;

import java.util.List;

public record RegistrarPromotorCommand(
        String nombre,
        String apellido,
        String doi,
        String correo,
        Long idInmobiliaria,
        List<Long> proyectosAsignados
) {
}
