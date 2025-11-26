package com.example.demo.application.dto.commands;

import com.example.demo.application.dto.UploadFileCommand;

import java.util.List;

public record RegistrarInmobiliariaCommand(
        String ruc,
        String razonSocial,
        List<String> nombreProyectos,
        UploadFileCommand logo

) {
}
