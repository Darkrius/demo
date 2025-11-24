package com.example.demo.api.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String codigo,
        String mensaje,
        LocalDateTime fecha
) {
}
