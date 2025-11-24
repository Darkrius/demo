package com.example.demo.application.service;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import com.example.demo.application.services.ListarAsesoresServiceImpl;
import com.example.demo.domain.dto.AsesorDashBoard;
import com.example.demo.domain.repository.AsesorRepository;
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
public class ListarAsesoresServiceImplTest {

    @Mock
    private AsesorRepository asesorRepository;

    @InjectMocks
    private ListarAsesoresServiceImpl service;

    @Test
    @DisplayName("Debe listar correctamente y mapear el total de registros")
    void listarExitoso() {
        // 1. GIVEN (Preparar datos simulados)
        // Simulamos que la BD devuelve 2 registros, pero dice que el total global es 20.
        AsesorDashBoard d1 = new AsesorDashBoard("1", "Juan", "Ica", "Hipo", 20L);
        AsesorDashBoard d2 = new AsesorDashBoard("2", "Ana", "Lima", "Hipo", 20L);

        when(asesorRepository.buscarPorNombreParcial("Juan", 1, 10))
                .thenReturn(List.of(d1, d2));

        // 2. WHEN (Ejecutar el servicio)
        PaginationResponseDTO<AsesorDashBoardDto> respuesta = service.listarAsesoresGestion(1, 10, "Juan");

        // 3. THEN (Verificar)
        assertNotNull(respuesta);
        assertEquals(2, respuesta.content().size()); // La página tiene 2 items
        assertEquals(20L, respuesta.totalElements()); // ¡El total global se extrajo bien!
        assertEquals("Ica", respuesta.content().get(0).ciudad()); // El mapeo de ciudad es correcto

        // Verificar que el campo sucio 'totalRegistros' NO existe en el DTO de respuesta (Visualmente)
        // (Esto lo garantiza el tipo de dato AsesorDashBoardDto)
    }

    @Test
    @DisplayName("Debe manejar lista vacía sin explotar")
    void listarVacio() {
        // 1. GIVEN
        when(asesorRepository.buscarPorNombreParcial(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of()); // Retorna vacío

        // 2. WHEN
        PaginationResponseDTO<AsesorDashBoardDto> respuesta = service.listarAsesoresGestion(1, 10, "");

        // 3. THEN
        assertEquals(0, respuesta.content().size());
        assertEquals(0L, respuesta.totalElements()); // Debe ser 0, no null
    }
}
