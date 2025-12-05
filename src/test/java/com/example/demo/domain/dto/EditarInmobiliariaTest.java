package com.example.demo.domain.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditarInmobiliariaTest {

    @Test
    @DisplayName("Debe crearse correctamente cuando todos los datos son válidos")
    void crearCasoExitoso() {
        // Given
        Long id = 100L;
        List<String> nombres = List.of("Torre A", "Torre B");
        List<Long> idsEliminar = List.of(5L);

        // When
        EditarInmobiliaria cmd = new EditarInmobiliaria(id, true, nombres, idsEliminar);

        // Then
        assertEquals(id, cmd.idInmobiliaria());
        assertTrue(cmd.estado());
        assertEquals(2, cmd.nombreProyectos().size());
        assertEquals(1, cmd.idProyectos().size());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID es nulo")
    void lanzarExcepcionIdNulo() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new EditarInmobiliaria(null, true, null, null));

        assertEquals("Para editar una inmobiliaria, el ID es obligatorio.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe convertir listas NULL en listas VACÍAS (Null Safety)")
    void manejarListasNulas() {
        // Given


        // When
        EditarInmobiliaria cmd = new EditarInmobiliaria(1L, true, null, null);

        // Then
        assertNotNull(cmd.nombreProyectos(), "La lista de nombres no debería ser null");
        assertTrue(cmd.nombreProyectos().isEmpty(), "La lista de nombres debería estar vacía");

        assertNotNull(cmd.idProyectos(), "La lista de IDs no debería ser null");
        assertTrue(cmd.idProyectos().isEmpty(), "La lista de IDs debería estar vacía");
    }

    @Test
    @DisplayName("Debe limpiar espacios (TRIM) y filtrar vacíos en los nombres")
    void limpiarNombresProyectos() {
        // Given: Una lista sucia con espacios y elementos vacíos
        List<String> nombresSucios = Arrays.asList("  Torre A  ", "", "   ", "Torre B", null);

        // When
        EditarInmobiliaria cmd = new EditarInmobiliaria(1L, true, nombresSucios, List.of());

        // Then
        List<String> nombresLimpios = cmd.nombreProyectos();

        assertEquals(2, nombresLimpios.size(), "Debería haber solo 2 nombres válidos");
        assertEquals("Torre A", nombresLimpios.get(0), "Debería haber hecho trim");
        assertEquals("Torre B", nombresLimpios.get(1));
        // Nota: Los vacíos, nulos y strings de solo espacios deben haber desaparecido
    }
}
