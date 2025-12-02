package com.example.demo.application.service;

import com.example.demo.application.dto.queries.PromotorDetalleDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.external.PromotorPortService;
import com.example.demo.application.services.DetallePromotorServiceImpl;
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
class DetallePromotorServiceImplTest {


    @Mock
    private PromotorPortService promotorPortService;

    @InjectMocks
    private DetallePromotorServiceImpl service;

    @Test
    @DisplayName("Debe retornar el detalle del promotor cuando existe")
    void listarPromotorExitoso() {
        // GIVEN
        Long idUsuario = 10L;

        PromotorDetalleDto.ProyectoAsignado proyecto1 =
                new PromotorDetalleDto.ProyectoAsignado(1L, "Residencial Los Robles");

        PromotorDetalleDto.ProspectoCaptado prospecto1 =
                new PromotorDetalleDto.ProspectoCaptado(100L, "Carlos Gómez", 1L, "Residencial Los Robles");

        PromotorDetalleDto dtoSimulado = new PromotorDetalleDto(
                10L,
                "Juan",
                "Pérez",
                "12345678",
                "correo@dominio.com",
                true,                // estado boolean
                5L,                  // idInmobiliaria
                "20123456789",       // ruc
                "Inmobiliaria Perú", // razonSocial
                List.of(proyecto1),
                List.of(prospecto1)
        );

        when(promotorPortService.listarPromotorPorId(idUsuario))
                .thenReturn(Optional.of(dtoSimulado));

        // WHEN
        PromotorDetalleDto resultado = service.listar(idUsuario);

        // THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.idUsuario());
        assertEquals("Juan", resultado.nombres());
        assertTrue(resultado.estado());
        assertEquals("20123456789", resultado.ruc());
        assertEquals(1, resultado.proyectosAsignados().size());
        assertEquals("Residencial Los Robles",
                resultado.proyectosAsignados().getFirst().nombreProyecto());

        verify(promotorPortService, times(1)).listarPromotorPorId(idUsuario);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el promotor no existe")
    void listarPromotorNoEncontrado() {
        // GIVEN
        Long idUsuario = 999L;

        when(promotorPortService.listarPromotorPorId(idUsuario))
                .thenReturn(Optional.empty());

        // WHEN - THEN
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.listar(idUsuario)
        );

        assertEquals("No existe un promotor con el id", exception.getMessage());
        verify(promotorPortService, times(1)).listarPromotorPorId(idUsuario);
    }
}
