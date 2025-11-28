package com.example.demo.domain.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PromotorTest {


    private DatosPersonales datosValidos() {
        return new DatosPersonales("Juan", "Perez", "12345678", "juan@caja.pe");
    }

    // -------------------------------------------------------------------
    // CASO 1: CREACIÓN BÁSICA Y DEFAULTS
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe crear un Promotor válido y asignar HIPOTECARIO por defecto si el tipo es nulo")
    void crearPromotor_Default() {
        // Given
        DatosPersonales datos = datosValidos();
        String tipoNulo = null;

        // When
        Promotor p = Promotor.registrar(datos, "ADMIN_01", 50L, tipoNulo, null);

        // Then
        assertNotNull(p);
        assertEquals("HIPOTECARIO", p.getTipoPromotor(), "Debe asignar default si es nulo");
        assertTrue(p.isEstado(), "Debe nacer activo");
        assertNotNull(p.getFechaCreacion(), "Debe tener fecha de auditoría");
        assertEquals(0, p.getProyectosAsignados().size(), "La lista debe nacer vacía si mandamos null");
    }

    // -------------------------------------------------------------------
    // CASO 2: ASIGNACIÓN DE TIPO EXPLÍCITO
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe respetar el tipo de promotor si se envía explícitamente")
    void crearPromotor_TipoExplicito() {
        // Given
        DatosPersonales datos = datosValidos();
        String tipoEsperado = "AGROPECUARIO";

        // When
        Promotor p = Promotor.registrar(datos, "ADMIN_01", 50L, tipoEsperado, null);

        // Then
        assertEquals(tipoEsperado, p.getTipoPromotor());
    }

    // -------------------------------------------------------------------
    // CASO 3: VALIDACIONES DE INTEGRIDAD (Value Object)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe fallar si el DOI tiene formato incorrecto (Validación de Value Object)")
    void crear_DoiInvalido() {
        // When & Then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new DatosPersonales("Juan", "Perez", "123", "c@c.com"); // DOI muy corto
        });

        assertEquals("El DOI debe tener 8 o 15 dígitos.", ex.getMessage());
    }

    @Test
    @DisplayName("Debe fallar si falta la Inmobiliaria (Validación de Entidad)")
    void crear_SinInmobiliaria() {
        // Given
        DatosPersonales datos = datosValidos();

        // When & Then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Promotor.registrar(datos, "ADMIN", null, "HIPO", null); // ID Inmo Nulo
        });

        assertEquals("El promotor debe pertenecer a una inmobiliaria.", ex.getMessage());
    }

    // -------------------------------------------------------------------
    // CASO 4: COMPORTAMIENTO (PROYECTOS)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe agregar proyectos y evitar duplicados")
    void agregarProyectos() {
        // Given
        Promotor p = Promotor.registrar(datosValidos(), "ADMIN", 1L, null, null);

        // When
        p.agregarProyecto(100L);
        p.agregarProyecto(200L);
        p.agregarProyecto(100L); // Intento de duplicado

        // Then
        List<Long> proyectos = p.getProyectosAsignados();
        assertEquals(2, proyectos.size(), "Debe tener 2 proyectos, ignorando el duplicado");
        assertTrue(proyectos.contains(100L));
        assertTrue(proyectos.contains(200L));
    }

    // -------------------------------------------------------------------
    // CASO 5: ASIGNAR ID (Post-Persistencia)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe permitir asignar el ID generado")
    void asignarId() {
        Promotor p = Promotor.registrar(datosValidos(), "ADMIN", 1L, null, null);
        p.asignarId(999L);
        assertEquals(999L, p.getIdUsuario());
    }


}
