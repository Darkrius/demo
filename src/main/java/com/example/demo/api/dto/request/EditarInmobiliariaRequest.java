package com.example.demo.api.dto.request;

import java.util.List;

public record EditarInmobiliariaRequest(
        Boolean estado,
        List<String> nombreProyectos,
        List<Long> idProyectos
) {
}
