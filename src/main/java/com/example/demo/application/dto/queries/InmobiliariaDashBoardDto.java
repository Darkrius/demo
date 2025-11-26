package com.example.demo.application.dto.queries;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InmobiliariaDashBoardDto(
        Long idImobiliara,
        String ruc,
        String razonSocial,
        int nProyectos,
        boolean estado,
        LocalDateTime fechaModificacion
) {
}
