package com.example.demo.domain.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InmobiliariasTest {

    @Test
    @DisplayName("Debe crear un Asesor válido con sus valores por defecto")
    void crearInmobiliariaValido(){
        Inmobiliarias i = Inmobiliarias.crear("20123456789", "Mi Empresa SAC", "ADMIN01", "http://s3/logo.png");

        assertNotNull(i);
        assertEquals("20123456789", i.getRuc());
        assertTrue(i.isEstado());
    }

    @Test
    @DisplayName("Debe fallar si el RUC no tiene 11 dígitos")
    void crear_RucInvalido() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Inmobiliarias.crear("123", "Empresa", "ADMIN", null);
        });

        assertEquals("El RUC debe tener exactamente 11 dígitos numéricos.", ex.getMessage());
    }

    @Test
    @DisplayName("Debe fallar si la Razón Social está vacía")
    void crear_RazonSocialVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            Inmobiliarias.crear("20123456789", "", "ADMIN", null);
        });
    }
}
