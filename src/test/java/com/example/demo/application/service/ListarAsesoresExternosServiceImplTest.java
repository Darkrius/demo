package com.example.demo.application.service;


import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorLegacyDto;
import com.example.demo.application.interfaces.external.AsesorLegacyService;
import com.example.demo.application.services.ListarAsesoresExternosServiceImpl;
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
class ListarAsesoresExternosServiceImplTest {

    @Mock
    private AsesorLegacyService asesorLegacyService;

    @InjectMocks
    private ListarAsesoresExternosServiceImpl service;

    @Test
    @DisplayName("Debe delegar correctamente los filtros al adaptador legacy")
    void listarExternosExitoso() {
        // 1. GIVEN (Escenario)
        int page = 1;
        int size = 10;
        String nombre = "Juan";
        String ciudad = "Ica";

        AsesorLegacyDto dto = new AsesorLegacyDto("A1", "Juan Perez", "Ica");
        PaginationResponseDTO<AsesorLegacyDto> respuestaSimulada = new PaginationResponseDTO<>(
                List.of(dto), 1, 10, 1, 1, true
        );

        when(asesorLegacyService.listar(page, size, nombre, ciudad))
                .thenReturn(respuestaSimulada);

        PaginationResponseDTO<AsesorLegacyDto> resultado = service.listarCandidatosExternos(page, size, nombre, ciudad);

        assertNotNull(resultado);
        assertEquals(1, resultado.totalElements());
        assertEquals("Juan Perez", resultado.content().get(0).nombreCompleto());

        // Verificamos que el servicio haya pasado los parámetros EXACTOS al adaptador
        verify(asesorLegacyService, times(1)).listar(page, size, nombre, ciudad);
    }

    @Test
    @DisplayName("Debe propagar la excepción si el adaptador falla")
    void listarConFalloEnInfra() {
        when(asesorLegacyService.listar(anyInt(), anyInt(), any(), any()))
                .thenThrow(new RuntimeException("Error de conexión DB Legacy"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.listarCandidatosExternos(1, 10, null, null);
        });

        assertEquals("Error de conexión DB Legacy", exception.getMessage());
    }
}
