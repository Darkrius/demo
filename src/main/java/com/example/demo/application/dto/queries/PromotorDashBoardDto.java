package com.example.demo.application.dto.queries;

import java.time.LocalDateTime;

public record PromotorDashBoardDto(
        Long idPromotor,
        String nombreCompleto,
        String nombreInmobiliaria,
        boolean estado,
        LocalDateTime fechaModificacion
) {
}
