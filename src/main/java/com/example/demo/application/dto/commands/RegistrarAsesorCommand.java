package com.example.demo.application.dto.commands;

public record RegistrarAsesorCommand(
        String idAsesorAD,
        String nombres,
        String apellidos,
        String correoCorporativo,
        String ciudad
) {
}
