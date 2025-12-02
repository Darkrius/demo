package com.example.demo.application.service;


import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.external.InmobiliariaPortService;
import com.example.demo.application.services.DetalleInmobiliariaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleInmobiliariaServiceImplTest {

    @Mock
    private InmobiliariaPortService inmobiliariaPortService;

    @InjectMocks
    private DetalleInmobiliariaServiceImpl service;

    @Test
    @DisplayName("Debe retornar el detalle de la inmobiliaria cuando existe")
    void listarInmobiliariaExitoso() {
        // GIVEN
        Long idInmobiliaria = 1L;

        InmobiliariaDetalleDto.ProyectosDto proyecto1 =
                new InmobiliariaDetalleDto.ProyectosDto(1L, "Proyecto A");

        InmobiliariaDetalleDto.PromotoresProyectoDto promotor1 =
                new InmobiliariaDetalleDto.PromotoresProyectoDto(1L, "Proyecto A", 10L, "Juan Pérez");

        InmobiliariaDetalleDto dtoSimulado = new InmobiliariaDetalleDto(
                idInmobiliaria,
                "20123456789",
                "Inmobiliaria Ejemplo",
                List.of(proyecto1),
                List.of(promotor1)
        );

        when(inmobiliariaPortService.listarInmobiliariaPorId(idInmobiliaria))
                .thenReturn(Optional.of(dtoSimulado));

        // WHEN
        InmobiliariaDetalleDto resultado = service.listar(idInmobiliaria);

        // THEN
        assertNotNull(resultado);
        assertEquals(idInmobiliaria, resultado.idInmobiliaria());
        assertEquals("20123456789", resultado.ruc());
        assertEquals("Inmobiliaria Ejemplo", resultado.razonSocial());
        assertEquals(1, resultado.proyectos().size());
        assertEquals("Proyecto A", resultado.proyectos().getFirst().nombreProyecto());
        assertEquals(1, resultado.promotoresProyectos().size());
        assertEquals("Juan Pérez", resultado.promotoresProyectos().getFirst().nombrePromotor());

        verify(inmobiliariaPortService, times(1)).listarInmobiliariaPorId(idInmobiliaria);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si la inmobiliaria no existe")
    void listarInmobiliariaNoEncontrada() {
        // GIVEN
        Long idInmobiliaria = 999L;

        when(inmobiliariaPortService.listarInmobiliariaPorId(idInmobiliaria))
                .thenReturn(Optional.empty());

        // WHEN - THEN
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.listar(idInmobiliaria)
        );

        assertEquals("No existe una inmobiliaria con el id", exception.getMessage());
        verify(inmobiliariaPortService, times(1)).listarInmobiliariaPorId(idInmobiliaria);
    }
}
