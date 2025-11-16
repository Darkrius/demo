package com.example.demo.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ProyectoRequestDto(
        @NotBlank(message = "El nombre del proyecto es obligatorio")
        String nombre
) {
}
