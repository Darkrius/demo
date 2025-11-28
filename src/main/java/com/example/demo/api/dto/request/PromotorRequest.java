package com.example.demo.api.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record PromotorRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,
        @NotBlank(message = "El apellido no puede estar vacío")
        String apellido,
        @NotBlank(message = "El DNI no puede estar vacío")
        @Pattern(regexp = "^\\d{8}$|^\\d{15}$", message = "El DOI debe tener exactamente 8 o 15 dígitos")
        String doi,
        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "El formato del correo es inválido")
        String correo,
        @NotNull(message = "Debe seleccionar una inmobiliaria")
        @Positive(message = "El ID de la inmobiliaria debe ser positivo")
        Long idInmobiliaria,
        @NotNull(message = "La lista de proyectos no puede ser nula")
        List<Long> proyectosAsignados
) {
}
