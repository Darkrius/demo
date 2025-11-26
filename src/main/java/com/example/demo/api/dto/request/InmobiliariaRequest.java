package com.example.demo.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record InmobiliariaRequest(
        @NotBlank(message = "El RUC es obligatorio")
        @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
        @Pattern(regexp = "\\d+", message = "El RUC solo debe contener números")
        String ruc,

        @NotBlank(message = "La razón social es obligatoria")
        String razonSocial,

        List<String> proyectos
) {
}
