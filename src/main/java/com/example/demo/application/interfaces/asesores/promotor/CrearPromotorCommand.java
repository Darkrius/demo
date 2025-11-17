package com.example.demo.application.interfaces.asesores.promotor;

import java.util.List;

public record CrearPromotorCommand(
        String doi,
        String nombre,
        String apellidos,
        String correo,
        Long idInmobiliaria,
        List<Long> idProyectos
) {
}
