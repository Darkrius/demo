package com.example.demo.domain.model;

public record DatosPersonales(String nombres,
                              String apellidos,
                              String doi,
                              String correo) {
    public DatosPersonales {
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        if (doi == null || (doi.length() != 8 && doi.length() != 15)) {
            throw new IllegalArgumentException("El DOI debe tener 8 o 15 dígitos.");
        }
        if (correo == null || !correo.contains("@")) {
            throw new IllegalArgumentException("El correo es inválido.");
        }
    }
}
