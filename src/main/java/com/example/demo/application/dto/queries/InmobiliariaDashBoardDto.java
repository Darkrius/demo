package com.example.demo.application.dto.queries;

import java.time.LocalDateTime;

public record InmobiliariaDashBoardDto(
        Long idInmobiliaria,
        String ruc,
        String razonSocial,
        int nProyectos,
        boolean estado,
        LocalDateTime fechaModificacion
) {
}
