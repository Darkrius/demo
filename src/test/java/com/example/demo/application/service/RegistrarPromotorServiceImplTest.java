package com.example.demo.application.service;


import com.example.demo.application.dto.commands.RegistrarPromotorCommand;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.interfaces.external.IEventPublisher;
import com.example.demo.application.services.RegistrarPromotorServiceImpl;
import com.example.demo.domain.model.Promotor;
import com.example.demo.domain.repository.InmobilariaRepository;
import com.example.demo.domain.repository.PromotorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RegistrarPromotorServiceImplTest {

    @Mock private PromotorRepository promotorRepository;
    @Mock private IEventPublisher eventPublisher;
    @Mock private InmobilariaRepository inmobilariaRepository;

    @InjectMocks private RegistrarPromotorServiceImpl service;

    // Helper para datos
    private RegistrarPromotorCommand comandoValido() {
        return new RegistrarPromotorCommand(
                "Juan", "Perez", "12345678", "juan@caja.pe", 10L, List.of(100L, 200L)
        );
    }

    @Test
    @DisplayName("Debe registrar promotor, asignar proyectos y publicar evento")
    void registrar_Exito() {
        // GIVEN
        RegistrarPromotorCommand cmd = comandoValido();

        // 2. CONFIGURAR EL COMPORTAMIENTO (STUB)
        // El servicio intenta buscar el nombre de la inmobiliaria ID 10, hay que decirle qué devolver.
        when(inmobilariaRepository.buscarRazonSocialPorId(10L))
                .thenReturn("Inmobiliaria Los Andes S.A.C.".describeConstable());

        when(promotorRepository.guardarPromotor(any(Promotor.class))).thenReturn(50L);

        // WHEN
        PromotorDashBoardDto resultado = service.registrar(cmd, "ADMIN_01");

        // THEN
        assertNotNull(resultado);
        assertEquals(50L, resultado.idPromotor());

        verify(promotorRepository).guardarPromotor(any());
        verify(eventPublisher).publicarEventoCreacion(any());

        // Opcional: Verificar que también llamó al repo de inmobiliaria
        verify(inmobilariaRepository).buscarRazonSocialPorId(10L);
    }

    @Test
    @DisplayName("Debe propagar EntidadDuplicadaException si el repo la lanza")
    void registrar_Duplicado() {
        RegistrarPromotorCommand cmd = comandoValido();
        when(promotorRepository.guardarPromotor(any()))
                .thenThrow(new EntidadDuplicadaException("DOI Duplicado"));

        assertThrows(EntidadDuplicadaException.class, () -> service.registrar(cmd, "ADMIN"));

        // Aseguramos que NO se intentó guardar hijos ni enviar evento
        verify(promotorRepository, never()).guardarProyectosPromotor(any(), any());
        verify(eventPublisher, never()).publicarEventoCreacion(any());
    }

}
