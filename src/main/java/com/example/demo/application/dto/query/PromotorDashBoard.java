package com.example.demo.application.dto.query;

import java.time.LocalDateTime;

public record PromotorDashBoard(
        Long idUsuario,
        String doi,
        String nombreCompleto,
        String nombreInmobiliaria,
        boolean estado,
        LocalDateTime fechaModificacion
) {
}


