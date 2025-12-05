package com.example.demo.application.service;

import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.application.interfaces.external.InmobiliariaPortService;
import com.example.demo.application.services.EditarInmobiliariaServiceImpl;
import com.example.demo.domain.dto.EditarInmobiliaria;
import com.example.demo.domain.repository.InmobilariaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class EditarInmobiliariaServiceImplTest {

    @Mock
    private InmobilariaRepository inmobilariaRepository;

    @Mock
    private InmobiliariaPortService inmobiliariaPortService;// Simulamos el puerto

    @InjectMocks
    private EditarInmobiliariaServiceImpl service; // Inyectamos el mock en el servicio real

    private EditarInmobiliaria comandoValido() {
        return new EditarInmobiliaria(10L, true, Collections.emptyList(), Collections.emptyList());
    }


    @Test
    @DisplayName("Debe guardar los cambios y devolver el DTO actualizado (Camino Feliz)")
    void editar_Exito() {
        // GIVEN
        EditarInmobiliaria cmd = comandoValido();

        // Simulamos un DTO de respuesta (puede ser mock o new)
        InmobiliariaDetalleDto dtoEsperado = mock(InmobiliariaDetalleDto.class);

        // Configuramos el comportamiento:
        // 1. El repositorio no hace nada (void)
        doNothing().when(inmobilariaRepository).guardarEdicion(cmd);

        // 2. El servicio externo devuelve el DTO actualizado
        when(inmobiliariaPortService.listarInmobiliariaPorId(cmd.idInmobiliaria()))
                .thenReturn(Optional.of(dtoEsperado));

        // WHEN
        InmobiliariaDetalleDto resultado = service.editar(cmd);

        // THEN
        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        // Verificamos el orden de ejecución (Importante en CQRS)
        verify(inmobilariaRepository).guardarEdicion(cmd); // Primero guarda
        verify(inmobiliariaPortService).listarInmobiliariaPorId(cmd.idInmobiliaria()); // Luego lee
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si tras editar no se encuentra la inmobiliaria")
    void editar_FalloRecuperacion() {
        // GIVEN
        EditarInmobiliaria cmd = comandoValido();

        // El guardado funciona...
        doNothing().when(inmobilariaRepository).guardarEdicion(cmd);

        // ...pero la recuperación falla (devuelve vacío)
        when(inmobiliariaPortService.listarInmobiliariaPorId(cmd.idInmobiliaria()))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RecursoNoEncontradoException.class, () -> service.editar(cmd));

        verify(inmobilariaRepository).guardarEdicion(cmd);
    }

    @Test
    @DisplayName("Debe propagar la excepción del repositorio si el guardado falla (sin intentar recuperar)")
    void editar_FalloGuardado() {
        // GIVEN
        EditarInmobiliaria cmd = comandoValido();

        // Simulamos error de negocio en el repositorio (ej. Duplicado o Regla)
        doThrow(new ReglasNegocioException("Error de negocio"))
                .when(inmobilariaRepository).guardarEdicion(cmd);

        // WHEN & THEN
        assertThrows(ReglasNegocioException.class, () -> service.editar(cmd));

        // Verificamos que NUNCA intentó llamar al servicio de lectura si falló el guardado
        verify(inmobiliariaPortService, never()).listarInmobiliariaPorId(any());
    }



}
