package com.example.demo.api.dto;

import java.time.LocalDateTime;

public record InmobiliariaResponse(
        Long idInmobiliaria,
        String ruc,
        String razonSocial,
        boolean estado,
        LocalDateTime fechaModificacion,
        int nProyectos
) {
}
