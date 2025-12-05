package com.example.demo.domain.dto;

import java.util.Collections;
import java.util.List;

public record EditarInmobiliaria(
        Long idInmobiliaria,
        Boolean estado,
        List<String> nombreProyectos,
        List<Long> idProyectos
) {

    public EditarInmobiliaria {

        if (idInmobiliaria == null) {
            throw new IllegalArgumentException("Para editar una inmobiliaria, el ID es obligatorio.");
        }

        if (nombreProyectos == null) {
            nombreProyectos = Collections.emptyList();
        } else {
            // Limpieza de datos (Trim) al vuelo
            nombreProyectos = nombreProyectos.stream()
                    .filter(n -> n != null && !n.isBlank())
                    .map(String::trim)
                    .toList();
        }

        if (idProyectos == null) {
            idProyectos = Collections.emptyList();
        }
    }
}
