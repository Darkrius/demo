package com.example.demo.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CrearInmobiliariaRequest(
        @NotBlank(message = "El RUC no puede estar vacío")
        @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
        String ruc,

        @Valid
        @NotEmpty(message = "Debe haber al menos un proyecto")
        List<ProyectoRequestDto> proyectos
) {
}
