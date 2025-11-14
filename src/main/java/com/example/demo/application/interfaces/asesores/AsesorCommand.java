package com.example.demo.application.interfaces.asesores;

public record AsesorCommand(String idAsesor,
                            String nombres,
                            String apellidos,
                            String correoCorporativo,
                            String ciudad) {
}
