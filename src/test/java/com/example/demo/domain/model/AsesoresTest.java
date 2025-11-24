package com.example.demo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsesoresTest {

    @Test
    @DisplayName("Debe crear un Asesor válido con sus valores por defecto")
    void crearAsesorValido() {
        // 1. Datos de prueba
        String id = "A001";
        String nombre = "Juan";
        String ubigeo = "140101"; // 6 dígitos

        // 2. Ejecución
        Asesores asesor = Asesores.registrar(id, nombre, "Perez", "juan@caja.pe", ubigeo);

        // 3. Verificación (Asserts)
        assertNotNull(asesor);
        assertEquals("A001", asesor.getIdAsesor());
        assertTrue(asesor.isEstado(), "El asesor debe nacer ACTIVO");
        assertEquals("Hipotecario", asesor.getTipoReferido(), "El tipo por defecto debe ser Hipotecario");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el Ubigeo no tiene 6 dígitos")
    void validarUbigeoIncorrecto() {
        // 1. Datos incorrectos (Ubigeo corto)
        String ubigeoInvalido = "1401";

        // 2. Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Asesores.registrar("A001", "Juan", "Perez", "juan@caja.pe", ubigeoInvalido);
        });

        // 3. Verificar el mensaje de error
        assertEquals("El ID de Ubigeo debe ser válido (6 caracteres).", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID es nulo")
    void validarIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            Asesores.registrar(null, "Juan", "Perez", "juan@caja.pe", "140101");
        });
    }
}
