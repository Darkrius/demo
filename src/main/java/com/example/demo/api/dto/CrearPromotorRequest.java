package com.example.demo.api.dto;

import java.util.List;

public record CrearPromotorRequest(
        String nombres,
        String apellidos,
        String doi,
        String correo,
        Long idInmobiliaria,
        List<Long> idProyectos
) {
}
