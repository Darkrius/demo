package com.example.demo.domain.dto;

import java.time.LocalDateTime;

public record PromotorDashBoard(
    Long idUsuario,
    String nombreCompleto,
    String nombreInmobiliaria,
    boolean estado,
    LocalDateTime fechaModificacion,
    long totalRegistros
) {
}
