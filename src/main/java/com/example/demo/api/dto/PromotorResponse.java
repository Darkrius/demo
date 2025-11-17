package com.example.demo.api.dto;

import java.time.LocalDateTime;

public record PromotorResponse(Long idPromotor,
                               String doi,
                               String nombreCompleto,
                               String nombreInmobiliaria,
                               boolean estado,
                               LocalDateTime fechaModificacion) {
}
