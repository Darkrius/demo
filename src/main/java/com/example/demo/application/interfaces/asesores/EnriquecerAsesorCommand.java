package com.example.demo.application.interfaces.asesores;

import java.util.List;

public record EnriquecerAsesorCommand(
        List<AsesorCommand> asesores
) {
}
