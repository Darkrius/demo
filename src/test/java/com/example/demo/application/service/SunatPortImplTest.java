package com.example.demo.application.service;




import com.example.demo.application.dto.DatosEmpresaDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.application.interfaces.external.SunatPort;
import com.example.demo.application.services.SunatPortImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SunatPortImplTest {

    @Mock
    private SunatPort sunatPort;

    @InjectMocks
    private SunatPortImpl service;

    @Test
    @DisplayName("Debe retornar el DTO con la Razón Social si el RUC existe")
    void consultarExito() {
        // 1. GIVEN
        String ruc = "20123456789";
        // Creamos el objeto que simula venir de la API externa (adaptador)
        DatosEmpresaDto dtoSimulado = new DatosEmpresaDto(ruc, "EMPRESA TEST S.A.C.");

        // El puerto devuelve un Optional del DTO, no un String
        when(sunatPort.consultarRuc(ruc)).thenReturn(Optional.of(dtoSimulado));

        // 2. WHEN
        DatosEmpresaDto resultado = service.obtenerRazonSocial(ruc);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals("EMPRESA TEST S.A.C.", resultado.razonSocial());
        assertEquals(ruc, resultado.ruc());
    }

    @Test
    @DisplayName("Debe lanzar ReglasNegocioException si el RUC es inválido (corto o letras)")
    void consultarRucInvalido() {
        // 1. GIVEN
        String rucMalo = "123"; // Corto

        // 2. WHEN & THEN
        assertThrows(ReglasNegocioException.class, () -> service.obtenerRazonSocial(rucMalo));

        // Aseguramos que NO se llamó al puerto externo
        verifyNoInteractions(sunatPort);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException (404) si el puerto devuelve vacío")
    void consultarNoEncontrado() {
        // 1. GIVEN
        String ruc = "20123456789";
        // El puerto devuelve vacío (no encontró nada en SUNAT)
        when(sunatPort.consultarRuc(ruc)).thenReturn(Optional.empty());

        // 2. WHEN & THEN
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerRazonSocial(ruc));
    }
}
