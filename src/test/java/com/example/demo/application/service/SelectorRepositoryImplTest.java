package com.example.demo.application.service;


import com.example.demo.application.dto.SelectorDto;
import com.example.demo.application.interfaces.external.ISelectorService;
import com.example.demo.application.services.SelectorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SelectorRepositoryImplTest {


    @Mock
    private ISelectorService selectorRepository;

    @InjectMocks
    private SelectorServiceImpl service;

    @Test
    @DisplayName("Debe retornar lista de inmobiliarias llamando al repositorio")
    void obtenerInmobiliarias_Exito() {
        // Given
        String adminId = "ADMIN";
        List<SelectorDto> mockList = List.of(new SelectorDto(1L, "Inmo Test"));
        when(selectorRepository.listarInmobiliarias(adminId)).thenReturn(mockList);

        // When
        List<SelectorDto> result = service.obtenerInmobiliaria(adminId);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1L, result.getFirst().id());
        verify(selectorRepository).listarInmobiliarias(adminId);
    }

    @Test
    @DisplayName("Debe retornar lista vacía si el ID de inmobiliaria es nulo (Protección)")
    void obtenerProyectos_IdNulo() {
        // Given
        Long idNulo = null;

        // When
        List<SelectorDto> result = service.obtenerProyecto(idNulo);

        // Then
        assertTrue(result.isEmpty());
        // Verificamos que NUNCA llamó a la base de datos (Ahorro de recursos)
        verifyNoInteractions(selectorRepository);
    }

    @Test
    @DisplayName("Debe delegar la búsqueda de proyectos al repositorio")
    void obtenerProyectos_Exito() {
        // Given
        Long idInmo = 50L;
        when(selectorRepository.listarProyectos(idInmo))
                .thenReturn(List.of(new SelectorDto(100L, "Proyecto Sol")));

        // When
        List<SelectorDto> result = service.obtenerProyecto(idInmo);

        // Then
        assertEquals("Proyecto Sol", result.getFirst().descripcion());
    }
}
