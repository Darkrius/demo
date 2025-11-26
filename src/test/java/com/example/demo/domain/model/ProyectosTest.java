package com.example.demo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProyectosTest {

    @Test
    @DisplayName("Debe crear un Proyecto válido y activo por defecto")
    void crearProyecto_Exito() {
        // Given & When
        Proyectos p = Proyectos.crear("Residencial Los Álamos");

        // Then
        assertNotNull(p);
        assertEquals("Residencial Los Álamos", p.getNombre());
        assertTrue(p.isEstado(), "El proyecto debe nacer con estado ACTIVO (true)");
    }

    @Test
    @DisplayName("Debe fallar si el nombre es nulo")
    void crear_NombreNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            Proyectos.crear(null);
        });
    }

    @Test
    @DisplayName("Debe fallar si el nombre está vacío o solo tiene espacios")
    void crear_NombreVacio() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Proyectos.crear("   ");
        });
        assertEquals("El nombre del proyecto es obligatorio.", ex.getMessage());
    }
}
