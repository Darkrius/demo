package com.example.demo.application.service;
import com.example.demo.application.dto.commands.RegistrarAsesorCommand;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.services.RegistrarAsesorServiceImpl;
import com.example.demo.domain.model.Asesores;
import com.example.demo.domain.repository.AsesorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarAsesorServiceImplTest {

    @Mock
    private AsesorRepository asesorRepository;

    @InjectMocks
    private RegistrarAsesorServiceImpl service;

    @Test
    @DisplayName("Debe registrar un asesor correctamente cuando la ciudad existe")
    void registrarExitoso() {
        RegistrarAsesorCommand command = new RegistrarAsesorCommand(
                "A001", "Juan", "Perez", "juan@mail.com", "Ica"
        );

        when(asesorRepository.buscarIdUbigeoPorCiudad("Ica"))
                .thenReturn(Optional.of("140101"));

        AsesorDashBoardDto resultado = service.registrarAsesor(command);

        assertNotNull(resultado);
        assertEquals("Ica", resultado.ciudad()); // Verificamos que devuelva la ciudad

        verify(asesorRepository, times(1)).guardar(any(Asesores.class));
    }

    @Test
    @DisplayName("Debe fallar si la ciudad no existe en el catálogo")
    void registrarConCiudadInexistente() {
        // 1. PREPARAR
        RegistrarAsesorCommand command = new RegistrarAsesorCommand(
                "A001", "Juan", "Perez", "juan@mail.com", "Narnia"
        );

        when(asesorRepository.buscarIdUbigeoPorCiudad("Narnia"))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(RecursoNoEncontradoException.class, () -> {
            service.registrarAsesor(command);
        });

        assertEquals("La ciudad indicada no existe en el catálogo: Narnia", ex.getMessage());

        verify(asesorRepository, never()).guardar(any());
    }
}
